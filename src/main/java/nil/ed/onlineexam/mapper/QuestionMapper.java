package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionMapper {
    int insert(Question record);

    List<Question> listQuestions(Integer pageStart, Integer pageSize);

    Integer countQuestions();

    List<Question> listQuestionsByCreator(Integer creator, Integer pageStart, Integer pageSize);

    Integer countQuestionsByCreator(Integer creator);

    Question getQuestionById(@Param("id") Integer id);

    Integer deleteQuestionById(@Param("id" )Integer id);

    Integer updateQuestion(@Param("question") Question question);

    List<Question> listQuestionsRandomly(@Param("n") Integer n, @Param("type") Integer type);
}