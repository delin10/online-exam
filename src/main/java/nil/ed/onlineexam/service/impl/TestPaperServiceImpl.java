package nil.ed.onlineexam.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import nil.ed.onlineexam.common.*;
import nil.ed.onlineexam.common.enumm.QuestionTypeEnum;
import nil.ed.onlineexam.common.enumm.TestPaperStatusEnum;
import nil.ed.onlineexam.delagate.CourseServiceDelegate;
import nil.ed.onlineexam.entity.*;
import nil.ed.onlineexam.mapper.*;
import nil.ed.onlineexam.service.ITestPaperService;
import nil.ed.onlineexam.service.getter.TestPaperServiceGetter;
import nil.ed.onlineexam.service.support.impl.SimpleInsertHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectOneHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectPageHelper;
import nil.ed.onlineexam.service.support.impl.SimpleUpdateHelper;
import nil.ed.onlineexam.util.PageUtils;
import nil.ed.onlineexam.util.ReflectionAsmUtils;
import nil.ed.onlineexam.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("testPaperService")
public class TestPaperServiceImpl implements ITestPaperService {
    private static final long FUTURE_GET_TIMEOUT = 3;

    private static final int MIN_TEST_DURATION = 30;;
    @Resource
    private TestPaperMapper testPaperMapper;

    @Resource
    private CourseServiceDelegate courseServiceDelegate;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private TestPaperContentItemMapper testPaperContentItemMapper;

    @Resource
    private SubmittedAnswerMapper submittedAnswerMapper;

    @Resource
    private JoinedTestMapper joinedTestMapper;

    private Executor executor;

    @Autowired
    @Qualifier("commonExecutor")
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    @MethodInvokeLog
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> addTestPaper(JSONObject testPaperJsonObject, Integer creator) {
        TestPaper testPaper = new TestPaper();
        ReflectionAsmUtils.copyProperties(testPaperJsonObject, testPaper);
        List<LinkedHashMap<String,Object>> options = TestPaperServiceGetter.getOptions(testPaperJsonObject);
        List<LinkedHashMap<String,Object>> subjectives = TestPaperServiceGetter.getSubjectives(testPaperJsonObject);
        checkProperties(testPaper, options, subjectives);

        if (!courseServiceDelegate.isValid(testPaper.getCid())){
            throw new IllegalArgumentException("课程不合法");
        }

        testPaper.setCreator(creator);

        return new SimpleInsertHelper()
                .operate(() -> {
                    testPaperMapper.insert(testPaper);
                    processTestPaperQuestion(options, testPaper.getId(), 1);
                    processTestPaperQuestion(subjectives, testPaper.getId(), 2);
                    return null;
                });
    }

    @Override
    public Response<TestPaperWithQuestionsVO> getTestPaper(Integer pid) {
        return new SimpleSelectOneHelper<TestPaperWithQuestionsVO>()
                .operate(() -> testPaperMapper.getTestPaperById(pid));
    }


    @Override
    public Response<CommonVO> generateTestPaperRandomly(Integer optionNum, Integer subjectiveNum) {
        return new SimpleSelectOneHelper<CommonVO>()
                .operate(() -> {
                    CompletableFuture<List<Question>> optionsFuture = CompletableFuture.supplyAsync(() ->
                            questionMapper.listQuestionsRandomly(optionNum, (int)QuestionTypeEnum.OPTION.getCode()));
                    CompletableFuture<List<Question>> subjectiveFuture = CompletableFuture.supplyAsync(() ->
                            questionMapper.listQuestionsRandomly(subjectiveNum, (int)QuestionTypeEnum.SUBJECTIVE.getCode()));

                    try {
                        return new CommonVO()
                                .add("options", optionsFuture.get(FUTURE_GET_TIMEOUT, TimeUnit.SECONDS))
                                .add("subjectives", subjectiveFuture.get(FUTURE_GET_TIMEOUT, TimeUnit.SECONDS));
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public Response<Void> publishedTest(Integer pid, Integer currentUser) {
        return new SimpleUpdateHelper()
                .operate(() ->
                        testPaperMapper.updatePublishedStatus(TestPaperStatusEnum.PUBLISHED.getCode(), pid, currentUser));
    }

    @Override
    public Response<Void> joinTest(Integer joiner, Integer tid) {
        /*
        判断tid是否合法以及用户能否参加该考试
         */
        TestPaper testPaper = testPaperMapper.getCanJoinedTestOrNULL(joiner,tid);
        if (Objects.isNull(testPaper)) {
            return new NormalResponseBuilder<Void>()
                    .setCode(ResponseCodeEnum.NOT_FOUND.getCode())
                    .setMessage(MessageFormat.format("无法找到能参加的课程，课程id:{0}", tid))
                    .build();
        }
        try {
        /*
        插入记录
         */
            JoinedTest joinedTest = new JoinedTest();
            joinedTest.setTid(tid);
            joinedTest.setUid(joiner);
            joinedTest.setUpdateTime(Instant.now().toEpochMilli());
            joinedTest.setCreateTime(Instant.now().toEpochMilli());
            joinedTestMapper.insert(joinedTest);
            return new NormalResponseBuilder<Void>()
                    .setCodeEnum(ResponseCodeEnum.SUCCESS)
                    .build();
        }catch (DuplicateKeyException e){
            return new NormalResponseBuilder<Void>()
                    .setCode(ResponseCodeEnum.FAILED.getCode())
                    .setMessage("你已经参加了该次考试,")
                    .build();
        }
    }

    @Override
    public Response<PageResult<UserTestVO>> listCanJoinOrHaveJoinedTests(Integer uid, Integer cid) {
        int pageNo = 0;
        int pageSize = 20;
        long currentTimeMilli = Instant.now().toEpochMilli();
        return new SimpleSelectPageHelper<UserTestVO>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(() -> testPaperMapper.countUserTestsOf(uid, null))
                .operate(() -> {
                    List<UserTestVO> testVOList = testPaperMapper.listUserTestsOf(uid, cid, null, PageUtils.calPageStart(pageNo, pageSize), pageSize);

                    testVOList.parallelStream().forEach(test -> {
                        /**
                         * 没参加考试而且考试已经截止
                         */
                        if ((test.getJoinTime() == null || test.getJoinTime() == 1) && test.getEndTime() > currentTimeMilli){
                            test.setTotalScore((short)0);
                            return;
                        }

                        test.setTotalScore(getTotalScore(uid,test.getId()));
                    });

                    return testVOList;
                });
    }

    @Override
    public Response<Void> submitAnswer(List<SubmittedAnswer> answerList, Integer pid, Integer userId) {
        checkTestValid(pid, userId);


        List<QuestionWithAnswerVO> questionWithAnswerVOList = testPaperMapper.listTestPaperQuestionsWithAnswer(pid);
        Map<Integer, List<QuestionWithAnswerVO>> questionVOMap = questionWithAnswerVOList.stream()
                .collect(Collectors.groupingBy(QuestionWithAnswerVO::getId));

        answerList.parallelStream().forEach(submittedAnswer -> processSubmittedAnswer(submittedAnswer, questionVOMap, pid, userId));

        addSubmittedAnswerListWithTransactional(answerList);

        return new NormalResponseBuilder<Void>()
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }

    @Override
    public Response<TestPaperWithQuestionWithSubmittedAnswerVO> getTestPaperWithQuestionWithSubmittedAnswerVO(Integer pid, Integer uid) {
        return new SimpleSelectOneHelper<TestPaperWithQuestionWithSubmittedAnswerVO>()
                .operate(() -> {
                    List<UserTestVO> userTestVOList = testPaperMapper.listUserTestsOf(uid,null, pid,0,1);
                    if (userTestVOList.isEmpty()){
                        throw new IllegalArgumentException("用户id下无该考试");
                    }

                    Long joinTime = userTestVOList.get(0).getJoinTime();
                    if (joinTime == null || joinTime <= 1){
                        throw new IllegalArgumentException("该用户未参加此考试");
                    }

                    List<QuestionWithAnswerVO> questionWithAnswerVOList = testPaperMapper.listTestPaperQuestionsWithAnswer(pid);


                    List<SubmittedAnswer> submittedAnswerList = testPaperMapper.listSubmittedAnswers(uid, pid);

                    if (submittedAnswerList == null || submittedAnswerList.isEmpty()){
                        return null;
                    }

                    return mapToTestPaperWithQuestionWithSubmittedAnswerVO(userTestVOList.get(0), questionWithAnswerVOList, submittedAnswerList);
                });
    }

    @Override
    public Response<Short> markTestPaper(Integer pid, Integer uid,Integer updater, JSONArray jsonArray) {
        TestPaperWithQuestionsVO testPaperWithQuestionsVO = testPaperMapper.getTestPaperById(pid);

        if (testPaperWithQuestionsVO == null){
            throw new IllegalArgumentException("pid不合法");
        }

        Map<Integer, List<TestPaperQuestionVO>> questionMap = testPaperWithQuestionsVO.getQuestions().stream()
                .collect(Collectors.groupingBy(QuestionVO::getId));
        jsonArray.parallelStream()
                .map(obj -> (LinkedHashMap)obj)
                .forEach(obj -> {
                    try{
                        Integer qid = Integer.valueOf(String.valueOf(obj.get("qid")));
                        Short score = Short.valueOf(String.valueOf(obj.get("score")));

                        List<TestPaperQuestionVO> questionVO = questionMap.get(qid);
                        if (questionVO != null && score >= 0 && score <= questionVO.get(0).getScore()) {
                            submittedAnswerMapper.updateSubjectiveQuestionScore(uid, pid, qid, score, updater);
                        }else{
                            throw new IllegalArgumentException("分数不能大于题目分数");
                        }
                    }catch (NumberFormatException e){ /* ignore */}
                });
        return new NormalResponseBuilder<Short>()
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .setData(getTotalScore(uid, pid))
                .build();
    }

    @Override
    public Response<ScoreStatisticVO> showScore(Integer pid, Integer uid) {
        List<UserTestVO> testList = testPaperMapper.listUserTestsOf(uid, null, pid, 0, 1);
        NormalResponseBuilder<ScoreStatisticVO> builder = new NormalResponseBuilder<>();

        if (testList.isEmpty()){
            return builder
                    .setCodeEnum(ResponseCodeEnum.NOT_FOUND)
                    .build();
        }

        UserTestVO testVO = testList.get(0);
        if (testVO.getJoinTime() == null || testVO.getJoinTime() <= 1){
            return builder
                    .setCodeEnum(ResponseCodeEnum.FAILED)
                    .build();
        }

        return builder
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .setData(statistic(uid, pid))
                .build();
    }

    @Override
    public Response<PageResult<TestPaper>> listOwnTestPaper(Integer uid) {
        Integer pageNo = 0, pageSize = Integer.MAX_VALUE;
        return new SimpleSelectPageHelper<TestPaper>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(() -> testPaperMapper.countOwnTestPapers(uid))
                .operate(() -> testPaperMapper.listOwnTestPapers(uid, PageUtils.calPageStart(pageNo, pageSize), pageSize));
    }

    private TestPaperWithQuestionWithSubmittedAnswerVO mapToTestPaperWithQuestionWithSubmittedAnswerVO(UserTestVO userTestVO,
                                                                                                       List<QuestionWithAnswerVO> testPaperWithQuestionsVO,
                                                                                                       List<SubmittedAnswer> submittedAnswerList){
        Map<Integer,List<SubmittedAnswer>> submittedAnswerMap = submittedAnswerList.stream().collect(Collectors.groupingBy(SubmittedAnswer::getQid));

        TestPaperWithQuestionWithSubmittedAnswerVO testPaper = new TestPaperWithQuestionWithSubmittedAnswerVO();
        testPaper.setId(userTestVO.getId());
        testPaper.setCid(userTestVO.getCid());
        testPaper.setCourseName(userTestVO.getCourseName());
        testPaper.setName(userTestVO.getName());
        testPaper.setStartTime(userTestVO.getStartTime());
        testPaper.setEndTime(userTestVO.getEndTime());
        testPaper.setTestDuration(userTestVO.getTestDuration());
        testPaper.setScore(userTestVO.getScore());

        List<QuestionWithSubmittedAnswerVO> questionWithSubmittedAnswerVOList = new LinkedList<>();
        testPaperWithQuestionsVO.forEach(question -> {
            QuestionWithSubmittedAnswerVO questionWithSubmittedAnswerVO = new QuestionWithSubmittedAnswerVO();

            questionWithSubmittedAnswerVO.setId(question.getId());
            questionWithSubmittedAnswerVO.setType(question.getType());
            questionWithSubmittedAnswerVO.setContent(question.getContent());
            questionWithSubmittedAnswerVO.setAnswer(question.getAnswer());
            questionWithSubmittedAnswerVO.setOptions(question.getOptions());
            questionWithSubmittedAnswerVO.setScore(question.getScore());
            List<SubmittedAnswer> submittedAnswers = submittedAnswerMap.get(question.getId());

            SubmittedAnswer submittedAnswer = Optional.of(submittedAnswers)
                    .map(submittedAnswerLs -> submittedAnswerLs.get(0))
                    .orElse(null);


            questionWithSubmittedAnswerVO.setSubmittedAnswer(Optional.ofNullable(submittedAnswer)
                    .map(SubmittedAnswer::getAnswer).orElse(""));
            questionWithSubmittedAnswerVO.setGainScore(Optional.ofNullable(submittedAnswer)
                    .map(SubmittedAnswer::getScore).orElse((short)0));

            questionWithSubmittedAnswerVOList.add(questionWithSubmittedAnswerVO);
        });

        testPaper.setQuestions(questionWithSubmittedAnswerVOList);
        return testPaper;
    }

    private Short getTotalScore(Integer uid, Integer pid){
        List<SubmittedAnswer> answerList = testPaperMapper.listSubmittedAnswers(uid, pid);

        boolean notYetMark = answerList.stream()
                .anyMatch(submittedAnswer -> submittedAnswer.getScore() < 0);

        if (!notYetMark){
            return (short) answerList.stream().mapToInt(SubmittedAnswer::getScore).sum();
        }

        return -1;
    }

    private ScoreStatisticVO statistic(Integer uid, Integer pid){
        List<SubmittedAnswer> answerList = testPaperMapper.listSubmittedAnswers(uid, pid);
        boolean notYetMark = answerList.stream()
                .anyMatch(submittedAnswer -> submittedAnswer.getScore() < 0);

        ScoreStatisticVO scoreStatisticVO = new ScoreStatisticVO();

        if (notYetMark){
            scoreStatisticVO.setTotalScore((short)-1);
            scoreStatisticVO.setSubjectiveScore((short)-1);
            scoreStatisticVO.setOptionScore((short)-1);
            return scoreStatisticVO;
        }

        List<QuestionWithAnswerVO> questionWithAnswerVOList = testPaperMapper.listTestPaperQuestionsWithAnswer(pid);
        Map<Integer, List<QuestionWithAnswerVO>> questionWithAnswerVOMap = questionWithAnswerVOList.stream()
                .collect(Collectors.groupingBy(QuestionWithAnswerVO::getId));


        Function<SubmittedAnswer, Byte> groupFunc = answer ->
                Optional.ofNullable(getFromMapList(questionWithAnswerVOMap, answer.getQid()))
                    .map(QuestionVO::getType)
                    .orElse((byte)-1);

        Map<Byte, List<SubmittedAnswer>> submittedAnswerMap = answerList.stream().collect(Collectors.groupingBy(groupFunc));

        Integer optionScore = Optional.ofNullable(submittedAnswerMap.get(QuestionTypeEnum.OPTION.getCode()))
                .map(submittedAnswerList -> submittedAnswerList.stream().mapToInt(SubmittedAnswer::getScore).sum())
                .orElse(0);

        Integer subjectiveScore = Optional.ofNullable(submittedAnswerMap.get(QuestionTypeEnum.SUBJECTIVE.getCode()))
                .map(submittedAnswerList -> submittedAnswerList.stream().mapToInt(SubmittedAnswer::getScore).sum())
                .orElse(0);


        scoreStatisticVO.setSubjectiveScore(subjectiveScore.shortValue());
        scoreStatisticVO.setOptionScore(optionScore.shortValue());
        scoreStatisticVO.setTotalScore((short)(subjectiveScore + optionScore));

        return scoreStatisticVO;
    }

    private <K,V> V getFromMapList(Map<K, List<V>> map, K key){
        List<V> result = map.getOrDefault(key, Collections.emptyList());

        if (result.isEmpty()){
            return null;
        }

        return result.get(0);
    }

    /**
     * 包裹在事务内的数据库插入
     * @param submittedAnswerList 答案列表
     * @return 插入条数
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addSubmittedAnswerListWithTransactional(List<SubmittedAnswer> submittedAnswerList){
        try {
            return testPaperMapper.addSubmittedAnswerList(submittedAnswerList);
        }catch (DuplicateKeyException e){
            throw new DuplicateKeyException("不能重复提交试卷");
        }
    }

    private void checkSingleSubmittedAnswer(SubmittedAnswer submittedAnswer){
        if (submittedAnswer.getQid() == null){
            throw new IllegalArgumentException("无题目标识");
        }
    }

    private void checkTestValid(Integer pid, Integer uid){
        Long currentTimeMilli = Instant.now().toEpochMilli();
        Integer pageNo = 0, pageSize = Integer.MAX_VALUE;

        List<UserTestVO> testPaper = testPaperMapper.listUserTestsOf(uid, null, pid, PageUtils.calPageStart(pageNo, pageSize), pageSize);

        if (testPaper == null || testPaper.isEmpty()){
            throw new IllegalArgumentException("考试场次不合法");
        }

        UserTestVO userTestVO = testPaper.get(0);
        if (userTestVO.getEndTime() < currentTimeMilli){
            throw new IllegalArgumentException("考试已过期");
        }

        if (userTestVO.getJoinTime() + userTestVO.getTestDuration() * 60 * 1000 < currentTimeMilli){
            throw new IllegalArgumentException("不能在考试时间过后提交答案！");
        }
    }

    /**
     * 处理提交的答案
     * @param submittedAnswer 提交的答案
     * @param pid 试卷id
     * @param userId 用户id
     */
    private void processSubmittedAnswer(SubmittedAnswer submittedAnswer,
                                        Map<Integer, List<QuestionWithAnswerVO>> questionVOMap,
                                        Integer pid,
                                        Integer userId){
        Long currentTimeMilli = Instant.now().toEpochMilli();
        Short initialScore = -1;


        checkSingleSubmittedAnswer(submittedAnswer);

        submittedAnswer.setCreateTime(currentTimeMilli);
        submittedAnswer.setId(null);
        submittedAnswer.setScore(initialScore);
        submittedAnswer.setUid(userId);
        submittedAnswer.setPid(pid);
        /**
         * 处理选择题分数
         */
        List<QuestionWithAnswerVO> questionVOList = questionVOMap.get(submittedAnswer.getQid());

        if (questionVOList == null){
            throw new IllegalArgumentException("存在非法题目,id="+submittedAnswer.getQid());
        }

        QuestionWithAnswerVO questionWithAnswerVO = questionVOList.get(0);

        if (questionWithAnswerVO.getType() == QuestionTypeEnum.OPTION.getCode()){
            if (questionWithAnswerVO.getAnswer().equals(submittedAnswer.getAnswer())) {
                submittedAnswer.setScore(questionWithAnswerVO.getScore());
            }else{
                submittedAnswer.setScore((short)0);
            }
        }
    }

    /**
     * 验证试卷信息是否合法
     *
     * @param testPaper 试卷信息
     */
    private void checkProperties(TestPaper testPaper,
                                 List<LinkedHashMap<String,Object>> options,
                                 List<LinkedHashMap<String,Object>> subjectives){
        if (testPaper.getScore() <= 0){
            throw new IllegalArgumentException("分数必须大于0");
        }

        if (testPaper.getStartTime() >= testPaper.getEndTime()){
            throw new IllegalArgumentException("考试结束时间必须大于开始时间");
        }

        if (testPaper.getTestDuration() < MIN_TEST_DURATION){
            throw new IllegalArgumentException("考试时长不能少于30分钟");
        }

        if (CollectionUtils.isEmpty(options)
                && CollectionUtils.isEmpty(subjectives)){
            throw new IllegalArgumentException("主观题和客观题不能同时为空");
        }

        if (sumScore(options) + sumScore(subjectives) != testPaper.getScore()){
            throw new IllegalArgumentException("题目总分和试卷总分不一致");
        }

        testPaper.setId(null);
        testPaper.setCreateTime(Instant.now().toEpochMilli());
    }

    /**
     * 计算题目总分
     * @param jsonObjects 包含总分的列表
     * @return 总分
     */
    private int sumScore(List<LinkedHashMap<String,Object>> jsonObjects){
        return  Optional.ofNullable(jsonObjects)
                .map(option -> option.stream()
                        .mapToInt(jsonObject -> Optional.ofNullable((String)jsonObject.get("score"))
                                .map(Integer::valueOf)
                                .orElseThrow(() -> new IllegalArgumentException("无题目分值参数")))
                        .sum())
                .orElse(0);
    }

    /**
     * 处理试卷问题
     * @param qids 问题列表
     * @param pid 试卷id
     * @param firstSeq 一级题目序号
     */
    private void processTestPaperQuestion(List<LinkedHashMap<String,Object>> qids, Integer pid, int firstSeq){
        IntStream.range(0, qids.size())
                .parallel()
                .forEach(i -> {
                    LinkedHashMap<String,Object> jsonObject = qids.get(i);
                    Integer score =  Integer.valueOf(jsonObject.get("score").toString());
                    Integer qid = (Integer) jsonObject.get("qid");
                    TestPaperContentItem testPaperContentItem
                            =mapToTestPaperContentItem(qid, score, pid, firstSeq, i);
                    testPaperContentItemMapper.insert(testPaperContentItem);
                });


    }

    /**
     * 将qid映射成条试卷题目记录
     * @param qid 题目id
     * @param score 分值
     * @param pid pid
     * @param firstSeq 一级题号
     * @param secSeq 二级题号
     * @return 试卷题目对象
     */
    private TestPaperContentItem mapToTestPaperContentItem(Integer qid,
                                                           Integer score,
                                                           Integer pid,
                                                           int firstSeq,
                                                           int secSeq){
        if (Objects.isNull(qid)){
            throw new IllegalArgumentException("存在非法的题目!");
        }

        Question question = questionMapper.getQuestionById(qid);

        if (Objects.isNull(question)){
            throw new IllegalArgumentException("存在非法的题目!");
        }

        TestPaperContentItem testPaperContentItem = new TestPaperContentItem();
        testPaperContentItem.setFirstSeq(firstSeq);
        testPaperContentItem.setSecSeq(secSeq);
        testPaperContentItem.setPid(pid);
        testPaperContentItem.setCreateTime(Instant.now().toEpochMilli());
        testPaperContentItem.setUpdateTime(Instant.now().toEpochMilli());
        testPaperContentItem.setScore(score);
        testPaperContentItem.setQid(question.getId());

        return testPaperContentItem;
    }

    public static void main(String[] args) {
        TestPaperServiceImpl testPaperService = new TestPaperServiceImpl();

        JSONArray ls = JSONArray.parseArray("[{\"qid\":1}]");
        ls.forEach(e -> {
            System.out.println(e.getClass());
        });

    }
}
