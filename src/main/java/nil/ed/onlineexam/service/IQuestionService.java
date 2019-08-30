package nil.ed.onlineexam.service;

import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.Question;
import nil.ed.onlineexam.entity.Resource;

import java.util.List;

/**
 * 题库接口
 */
public interface IQuestionService {
    /**
     * 分页查看所有题目
     * @param pageNo 页号
     * @param pageSize 页大小
     * @return 响应对象
     */
    Response<PageResult<Question>> listQuestions(Integer pageNo, Integer pageSize);

    /**
     * 查看creator创建的题目
     * @param creator 创建者
     * @param pageNo 页号
     * @param pageSize 页大小
     * @return 响应对象
     */
    Response listQuestionsByCreator(Integer creator, Integer pageNo, Integer pageSize);

    /**
     * 添加题目
     * @param question 题目
     * @param creator 创建者
     * @return 响应对象
     */
    Response addQuestion(Question question, Integer creator);

    /**
     * 更新题目内容
     * @param question 题目
     * @param updater 更新者
     * @return 响应对象
     */
    Response updateQuestion(Question question, Integer updater);


}
