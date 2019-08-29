package nil.ed.onlineexam.common;

import lombok.Data;

@Data
public class Response {
    /*
     code
    */
    private int code;

    /*
    the message related to code
     */
    private String message;

    /*
    the payload
     */
    private Object data;
}
