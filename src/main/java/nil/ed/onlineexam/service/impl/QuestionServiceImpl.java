package nil.ed.onlineexam.service.impl;

import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
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
    public Response<PageResult<Question>> listQuestions(Integer pageNo, Integer pageSize) {
        return new SimpleSelectPageHelper<Question>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(()-> questionMapper.countQuestions())
                .operate(() -> questionMapper.listQuestions(PageUtils.calPageStart(pageNo, pageSize), pageSize));
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
    }

    private void setUpdateFields(Question question, Integer updater){
        question.setUpdateTime(Instant.now().toEpochMilli());
        question.setUpdater(updater);
    }
}
