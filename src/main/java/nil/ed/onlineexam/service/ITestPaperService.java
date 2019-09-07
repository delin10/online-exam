package nil.ed.onlineexam.service;

import com.alibaba.fastjson.JSONObject;
import nil.ed.onlineexam.common.CommonVO;
import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.vo.TestPaperWithQuestionsVO;
import nil.ed.onlineexam.vo.UserTestVO;

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
    Response<PageResult<UserTestVO>> listCanJoinOrHaveJoinedTests(Integer uid);
}
