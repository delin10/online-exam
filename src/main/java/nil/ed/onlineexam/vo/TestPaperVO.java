package nil.ed.onlineexam.vo;

import lombok.Data;

import java.util.List;

@Data
public class TestPaperVO {
    private Integer id;

    private Integer cid;

    private String name;

    private Long startTime;

    private Long endTime;

    private Integer testDuration;

    private Integer score;

    private List<TestPaperQuestionVO> questions;

}
