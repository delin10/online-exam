package nil.ed.onlineexam.service.impl;

import com.alibaba.fastjson.JSONObject;
import nil.ed.onlineexam.common.CommonVO;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.enumm.QuestionTypeEnum;
import nil.ed.onlineexam.common.enumm.TestPaperStatusEnum;
import nil.ed.onlineexam.delagate.CourseServiceDelegate;
import nil.ed.onlineexam.entity.Question;
import nil.ed.onlineexam.entity.TestPaper;
import nil.ed.onlineexam.entity.TestPaperContentItem;
import nil.ed.onlineexam.mapper.QuestionMapper;
import nil.ed.onlineexam.mapper.TestPaperContentItemMapper;
import nil.ed.onlineexam.mapper.TestPaperMapper;
import nil.ed.onlineexam.service.ITestPaperService;
import nil.ed.onlineexam.service.getter.TestPaperServiceGetter;
import nil.ed.onlineexam.service.support.impl.SimpleInsertHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectOneHelper;
import nil.ed.onlineexam.service.support.impl.SimpleUpdateHelper;
import nil.ed.onlineexam.util.ReflectionAsmUtils;
import nil.ed.onlineexam.vo.TestPaperVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> addTestPaper(JSONObject testPaperJsonObject, Integer creator) {
        TestPaper testPaper = new TestPaper();
        ReflectionAsmUtils.copyProperties(testPaperJsonObject, testPaper);
        List<JSONObject> options = TestPaperServiceGetter.getOptions(testPaperJsonObject);
        List<JSONObject> subjectives = TestPaperServiceGetter.getSubjectives(testPaperJsonObject);
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
    public Response<TestPaperVO> getTestPaper(Integer pid) {
        return new SimpleSelectOneHelper<TestPaperVO>()
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

    /**
     * 验证试卷信息是否合法
     *
     * @param testPaper 试卷信息
     */
    private void checkProperties(TestPaper testPaper,
                                 List<JSONObject> options,
                                 List<JSONObject> subjectives){
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
    private int sumScore(List<JSONObject> jsonObjects){
        return  Optional.ofNullable(jsonObjects)
                .map(option -> option.stream()
                        .mapToInt(jsonObject -> Optional.ofNullable(jsonObject.getInteger("score"))
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
    private void processTestPaperQuestion(List<JSONObject> qids, Integer pid, int firstSeq){
        IntStream.range(0, qids.size())
                .parallel()
                .forEach(i -> {
                    JSONObject jsonObject = qids.get(i);
                    Integer score = jsonObject.getInteger("score");
                    Integer qid = jsonObject.getInteger("qid");
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
}
