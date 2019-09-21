package nil.ed.onlineexam.vo;

import lombok.Data;

@Data
public class UserTestVO extends BaseTestPaperVO {
    private Long joinTime;

    private Short totalScore;
}
