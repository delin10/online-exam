package nil.ed.onlineexam.vo;

import lombok.Data;

@Data
public class QuestionVO {
    private Integer id;

    private Byte type;

    private String content;

    private String options;
}
