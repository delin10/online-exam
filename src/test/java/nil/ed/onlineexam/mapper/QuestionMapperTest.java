package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.AbstractServiceTest;
import nil.ed.onlineexam.common.enumm.QuestionTypeEnum;
import nil.ed.onlineexam.entity.Question;
import org.junit.Test;

import javax.annotation.Resource;
import java.time.Instant;

public class QuestionMapperTest extends AbstractServiceTest {
    @Resource
    private QuestionMapper mapper;

    @Test
    public void insert() {
        Question question = new Question();
        question.setType(QuestionTypeEnum.OPTION.getCode());

        question.setContent("请问...?");
        question.setAnswer("A");
        question.setCreator(2);
        question.setUpdater(2);
        question.setUpdateTime(Instant.now().toEpochMilli());
        question.setCreateTime(Instant.now().toEpochMilli());
        question.setOptions("A:1,B:2,C:3,D:4");

        mapper.insert(question);
        question.setType(QuestionTypeEnum.SUBJECTIVE.getCode());
        mapper.insert(question);
    }

    @Test
    public void listQuestions() {
        printAsJsonString(mapper.listQuestions());
    }

    @Test
    public void getQuestionById() {
        printAsJsonString(mapper.getQuestionById(2));
    }

    @Test
    public void deleteQuestionById() {
        printAsJsonString(mapper.deleteQuestionById(1));
    }

    @Test
    public void updateQuestion() {
        Question question = new Question();
        question.setId(3);
        question.setType(QuestionTypeEnum.OPTION.getCode());

        question.setContent("请问...?修改");
        question.setUpdater(2);
        question.setUpdateTime(Instant.now().toEpochMilli());
        printAsJsonString(mapper.updateQuestion(question));
    }
}