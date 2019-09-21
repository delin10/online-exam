package nil.ed.onlineexam.controller.handler;


import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.util.ExceptionUtils;
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
    @MethodInvokeLog
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<String> defaultExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_OK);
        e.printStackTrace();
        return new NormalResponseBuilder<String>()
                .setCodeEnum(ResponseCodeEnum.UNCAUGHT_EXCEPTION)
                .setData(ExceptionUtils.getRootMessage(e))
                .build();
    }


}