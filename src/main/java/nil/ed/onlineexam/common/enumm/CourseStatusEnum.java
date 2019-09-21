package nil.ed.onlineexam.common.enumm;

/**
 * 课程状态的枚举
 */
public enum CourseStatusEnum {
    READY((byte)0,"未开始"),
    ON((byte)1, "进行中"),
    TESTING((byte)2, "考试中"),
    PUBLISHED_GRADE((byte)3, "发布成绩"),
    OVER((byte)4, "结课");
    private byte code;
    private String value;

    CourseStatusEnum(byte code, String value) {
        this.code = code;
        this.value = value;
    }

    public byte getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
