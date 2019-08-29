package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.Question;

public interface QuestionMapper {
    int insert(Question record);

    int insertSelective(Question record);
}