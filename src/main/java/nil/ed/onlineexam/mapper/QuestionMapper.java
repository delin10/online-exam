package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionMapper {
    int insert(Question record);

    List<Question> listQuestions();

    Question getQuestionById(@Param("id") Integer id);

    Integer deleteQuestionById(@Param("id" )Integer id);

    Integer updateQuestion(@Param("question") Question question);
}