package nil.ed.onlineexam.vo;

import lombok.Data;

@Data
public class TestPaperQuestionVO extends QuestionVO {
    private Short firstSeq;

    private Short secSeq;

    private int score;
}
