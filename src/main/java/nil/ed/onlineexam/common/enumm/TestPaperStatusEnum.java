package nil.ed.onlineexam.common.enumm;

public enum  TestPaperStatusEnum {
    UNPUBLISHED((byte)0, "未发布"),
    PUBLISHED((byte)1,"已发布");
    private byte code;
    private String value;

    TestPaperStatusEnum(byte code, String value) {
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
