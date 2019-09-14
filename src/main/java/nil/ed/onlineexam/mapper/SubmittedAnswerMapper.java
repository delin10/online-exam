package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.SubmittedAnswer;
import org.apache.ibatis.annotations.Param;

public interface SubmittedAnswerMapper {
    int insert(SubmittedAnswer record);

    int insertSelective(SubmittedAnswer record);

    Integer updateSubjectiveQuestionScore(@Param("uid") Integer uid,
                                          @Param("pid") Integer pid,
                                          @Param("qid") Integer qid,
                                          @Param("score") Short score,
                                          @Param("updater") Integer updater);
}