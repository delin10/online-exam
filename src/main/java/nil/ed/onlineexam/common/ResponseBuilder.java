package nil.ed.onlineexam.common;

/**
 * a response builder
 * @param <T> data type
 */
public abstract class ResponseBuilder<T> {
    protected int code;
    protected String message;
    protected T data;

    public int getCode() {
        return code;
    }

    public ResponseBuilder<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public ResponseBuilder<T> setCodeEnum(ResponseCodeEnum responseCodeEnum){
        this.setCode(responseCodeEnum.getCode())
            .setMessage(responseCodeEnum.getMessage());
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseBuilder<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseBuilder<T> setData(T data) {
        this.data = data;
        return this;
    }

    public abstract Response<T> build();
}
