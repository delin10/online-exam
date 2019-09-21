package nil.ed.onlineexam.vo;

import lombok.Data;

import java.util.List;

@Data
public class TestPaperWithQuestionWithSubmittedAnswerVO extends BaseTestPaperVO {
    private List<QuestionWithSubmittedAnswerVO> questions;
}
