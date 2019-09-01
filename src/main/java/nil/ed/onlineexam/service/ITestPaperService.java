package nil.ed.onlineexam.service;

import com.alibaba.fastjson.JSONObject;
import nil.ed.onlineexam.common.CommonVO;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.TestPaper;
import nil.ed.onlineexam.vo.TestPaperVO;

import java.awt.image.RescaleOp;

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
    Response<TestPaperVO> getTestPaper(Integer pid);

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
}
