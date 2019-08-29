package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.SubmittedAnswer;

public interface SubmittedAnswerMapper {
    int insert(SubmittedAnswer record);

    int insertSelective(SubmittedAnswer record);
}