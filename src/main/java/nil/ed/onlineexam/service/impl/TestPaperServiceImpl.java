package nil.ed.onlineexam.service.impl;

import com.alibaba.fastjson.JSONObject;
import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import nil.ed.onlineexam.common.*;
import nil.ed.onlineexam.common.enumm.QuestionTypeEnum;
import nil.ed.onlineexam.common.enumm.TestPaperStatusEnum;
import nil.ed.onlineexam.delagate.CourseServiceDelegate;
import nil.ed.onlineexam.entity.JoinedTest;
import nil.ed.onlineexam.entity.Question;
import nil.ed.onlineexam.entity.TestPaper;
import nil.ed.onlineexam.entity.TestPaperContentItem;
import nil.ed.onlineexam.mapper.JoinedTestMapper;
import nil.ed.onlineexam.mapper.QuestionMapper;
import nil.ed.onlineexam.mapper.TestPaperContentItemMapper;
import nil.ed.onlineexam.mapper.TestPaperMapper;
import nil.ed.onlineexam.service.ITestPaperService;
import nil.ed.onlineexam.service.getter.TestPaperServiceGetter;
import nil.ed.onlineexam.service.support.impl.SimpleInsertHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectOneHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectPageHelper;
import nil.ed.onlineexam.service.support.impl.SimpleUpdateHelper;
import nil.ed.onlineexam.util.PageUtils;
import nil.ed.onlineexam.util.ReflectionAsmUtils;
import nil.ed.onlineexam.vo.BaseTestPaperVO;
import nil.ed.onlineexam.vo.TestPaperWithQuestionsVO;
import nil.ed.onlineexam.vo.UserTestVO;
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
    public Response<PageResult<UserTestVO>> listCanJoinOrHaveJoinedTests(Integer uid) {
        int pageNo = 0;
        int pageSize = 20;
        return new SimpleSelectPageHelper<UserTestVO>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(() -> testPaperMapper.countUserTestsOf(uid, null))
                .operate(() -> testPaperMapper.listUserTestsOf(uid, null, PageUtils.calPageStart(pageNo, pageSize), pageSize));
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

        JSONObject ls = JSONObject.parseObject("{\"score\":\"100\",\"startTime\":1568044800000,\"endTime\":1568131200000,\"testDuration\":\"60\",\"options\":[{\"qid\":99,\"score\":\"25\"},{\"qid\":244,\"score\":\"25\"}],\"subjectives\":[{\"qid\":99,\"score\":\"25\"},{\"qid\":244,\"score\":\"25\"}]}");
        System.out.println(

        );

    }
}
