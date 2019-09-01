package nil.ed.onlineexam.service.exception;

public class VerifyInfoIncorrectException extends Exception {
    public VerifyInfoIncorrectException() {
    }

    public VerifyInfoIncorrectException(String message) {
        super(message);
    }

    public VerifyInfoIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerifyInfoIncorrectException(Throwable cause) {
        super(cause);
    }

    public VerifyInfoIncorrectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
