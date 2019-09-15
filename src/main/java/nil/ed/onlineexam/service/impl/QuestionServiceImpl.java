package nil.ed.onlineexam.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.enumm.QuestionTypeEnum;
import nil.ed.onlineexam.entity.Question;
import nil.ed.onlineexam.mapper.QuestionMapper;
import nil.ed.onlineexam.service.IQuestionService;
import nil.ed.onlineexam.service.support.impl.SimpleInsertHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectPageHelper;
import nil.ed.onlineexam.service.support.impl.SimpleUpdateHelper;
import nil.ed.onlineexam.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.concurrent.Executor;

@Service("questionService")
public class QuestionServiceImpl implements IQuestionService {
    @Resource
    private QuestionMapper questionMapper;

    private Executor executor;

    @Autowired
    @Qualifier("commonExecutor")
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Response<PageResult<Question>> listQuestions(Integer pageNo, Integer pageSize, Integer type) {
        return new SimpleSelectPageHelper<Question>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(()-> questionMapper.countQuestions(type))
                .operate(() -> questionMapper.listQuestions(PageUtils.calPageStart(pageNo, pageSize), pageSize, type));
    }

    @Override
    public Response listQuestionsByCreator(Integer creator, Integer pageNo, Integer pageSize) {
        return new SimpleSelectPageHelper<Question>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(() -> questionMapper.countQuestionsByCreator(creator))
                .operate(() ->
                        questionMapper.listQuestionsByCreator(creator, PageUtils.calPageStart(pageNo, pageSize), pageSize));
    }

    @Override
    public Response addQuestion(Question question, Integer creator) {
        checkQuestionRequired(question);
        setCreateFields(question, creator);
        return new SimpleInsertHelper()
                .operate(() -> {
                    questionMapper.insert(question);
                    return null;
                });
    }

    @Override
    public Response updateQuestion(Question question, Integer updater) {
        setUpdateFields(question, updater);
        return new SimpleUpdateHelper()
                .operate(() -> questionMapper.updateQuestion(question));
    }


    private void setCreateFields(Question question, Integer creator){
        question.setCreateTime(Instant.now().toEpochMilli());
        question.setCreator(creator);
        question.setUpdateTime(question.getCreateTime());
        question.setUpdater(creator);
    }

    private void setUpdateFields(Question question, Integer updater){
        question.setUpdateTime(Instant.now().toEpochMilli());
        question.setUpdater(updater);
    }

    private void checkQuestionRequired(Question question){
        if (question.getContent() == null){
            throw new IllegalArgumentException("题目内容不能为空");
        }

        if (question.getType() == null){
            throw new IllegalArgumentException("模糊的题目类型");
        }

        if (question.getAnswer() == null || question.getAnswer().trim().isEmpty()){
            throw new IllegalArgumentException("题目答案为空");
        }

        if (question.getType() == QuestionTypeEnum.OPTION.getCode()){
            if (question.getOptions() == null || question.getOptions().trim().isEmpty()){
                throw new IllegalArgumentException("题目选项为空");
            }
            JSONArray options = null;
            try{
                options = JSONArray.parseArray(question.getOptions());

                Integer answerIndex = Integer.valueOf(question.getAnswer().trim());
                if (answerIndex < 0 || answerIndex >= options.size()){
                    throw new IllegalArgumentException("选择题答案不合法");
                }
            }catch (JSONException e){
                throw  new IllegalArgumentException("选项格式错误");
            }catch (NumberFormatException e){
                throw new IllegalArgumentException("选项答案错误");
            }

        }
    }
}
