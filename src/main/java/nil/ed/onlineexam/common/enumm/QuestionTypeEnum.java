package nil.ed.onlineexam.common.enumm;

public enum QuestionTypeEnum {
    OPTION((byte)0,"选择题"),
    SUBJECTIVE((byte)1, "主观题");
    private byte code;
    private String value;

    QuestionTypeEnum(byte code, String value) {
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
