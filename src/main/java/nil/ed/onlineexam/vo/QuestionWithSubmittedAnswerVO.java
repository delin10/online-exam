package nil.ed.onlineexam.vo;

import lombok.Data;

@Data
public class QuestionWithSubmittedAnswerVO extends QuestionWithAnswerVO {
    private String submittedAnswer;

    private Short gainScore;
}
