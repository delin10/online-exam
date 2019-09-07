package nil.ed.onlineexam.vo;

import lombok.Data;

@Data
public class BaseTestPaperVO {
    private Integer id;

    private Integer cid;

    private String courseName;

    private String name;

    private Long startTime;

    private Long endTime;

    private Integer testDuration;

    private Integer score;
}
