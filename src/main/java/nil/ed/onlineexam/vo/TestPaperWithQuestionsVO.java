package nil.ed.onlineexam.vo;

import lombok.Data;

import java.util.List;

@Data
public class TestPaperWithQuestionsVO extends BaseTestPaperVO {
    private Integer score;

    private List<TestPaperQuestionVO> questions;

    private Long joinTime;

}
