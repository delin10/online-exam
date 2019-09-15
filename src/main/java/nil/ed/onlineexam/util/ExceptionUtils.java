package nil.ed.onlineexam.util;

public class ExceptionUtils {
    /**
     * 获得抛出点的异常消息
     * @param throwable
     * @return
     */
    public static String getRootMessage(Throwable throwable){
        Throwable current = throwable;
        while (current.getCause() != null){
            current = current.getCause();
        }

        return current.getMessage();
    }
}
