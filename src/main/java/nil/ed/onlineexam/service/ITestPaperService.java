package nil.ed.onlineexam.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import nil.ed.onlineexam.common.CommonVO;
import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.SubmittedAnswer;
import nil.ed.onlineexam.entity.TestPaper;
import nil.ed.onlineexam.vo.ScoreStatisticVO;
import nil.ed.onlineexam.vo.TestPaperWithQuestionWithSubmittedAnswerVO;
import nil.ed.onlineexam.vo.TestPaperWithQuestionsVO;
import nil.ed.onlineexam.vo.UserTestVO;

import java.util.List;

/**
 * 试卷管理
 */
public interface ITestPaperService {
    /**
     * 创建试卷
     * @param testPaper 试卷
     * @return 响应对象
     */
    Response<Void> addTestPaper(JSONObject testPaper, Integer creator);

    /**
     * 获取已发布的试卷信息
     * @param pid 试卷id
     * @return 响应对象
     */
    Response<TestPaperWithQuestionsVO> getTestPaper(Integer pid);

    /**
     * 随机生成试卷
     * 如果题库里没有足够题目，同样正常返回
     * @return
     */
    Response<CommonVO> generateTestPaperRandomly(Integer optionNum, Integer subjectiveNum);

    /**
     * 发布考试信息
     * @param pid 试卷id
     * @param currentUser 当前登录用户，只有创建人能发布
     * @return
     */
    Response<Void> publishedTest(Integer pid, Integer currentUser);

    /**
     * 参加考试
     * @param joiner 参加考试的学生
     * @param tid 试卷id
     * @return 参加结果
     */
    Response<Void> joinTest(Integer joiner, Integer tid);

    /**
     * 获取uid用户可以参加或者已经参加的所有考试信息
     * @param uid 用户id
     * @return 考试信息分页列表
     */
    Response<PageResult<UserTestVO>> listCanJoinOrHaveJoinedTests(Integer uid, Integer cid);

    /**
     * 处理提交的答案，先进行客观题的打分
     *
     * @param userId 用户id
     * @param answerList 答案
     * @return 结果信息
     */
    Response<Void> submitAnswer(List<SubmittedAnswer> answerList, Integer pid, Integer userId);

    /**
     * 获取带有用户答题情况试卷信息
     *
     * @param pid 试卷id
     * @param uid 用户id
     * @return 试卷
     */
    Response<TestPaperWithQuestionWithSubmittedAnswerVO> getTestPaperWithQuestionWithSubmittedAnswerVO(Integer pid, Integer uid);

    /**
     * 批改试卷
     * @param pid 试卷id
     * @param uid 考试学生的id
     * @param updater 更新人
     * @param jsonArray 提交的分数和题目
     * @return 总分数
     */
    Response<Short> markTestPaper(Integer pid, Integer uid, Integer updater, JSONArray jsonArray);


    /**
     * 查看uid用户pid试卷的成绩
     * @param pid 试卷id
     * @param uid 用户id
     * @return 成绩
     */
    Response<ScoreStatisticVO> showScore(Integer pid, Integer uid);

    /**
     * 查看用户名下考试
     * @param uid 用户id
     * @return 考试
     */
    Response<PageResult<TestPaper>> listOwnTestPaper(Integer uid);
}
