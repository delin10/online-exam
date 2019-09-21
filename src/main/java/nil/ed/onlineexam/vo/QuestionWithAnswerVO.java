package nil.ed.onlineexam.vo;

import lombok.Data;

@Data
public class QuestionWithAnswerVO extends QuestionVO {
    private String answer;
    private Short score;
}
