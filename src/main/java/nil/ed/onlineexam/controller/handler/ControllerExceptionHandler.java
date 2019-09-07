package nil.ed.onlineexam.controller.handler;


import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

/**
 * @author lidelin
 * @date 2019/07/29 18:31
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    // 声明要捕获的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<String> defultExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new NormalResponseBuilder<String>()
                .setCodeEnum(ResponseCodeEnum.UNCAUGHT_EXCEPTION)
                .setData(e.getMessage())
                .build();
    }
}