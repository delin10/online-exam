package nil.ed.onlineexam.security;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.util.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("fail");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        Response<String> res = new NormalResponseBuilder<String>()
                .setCodeEnum(ResponseCodeEnum.ACCESS_DENIED)
                .setData(ExceptionUtils.getRootMessage(exception))
                .build();

        response.getWriter().println(JSON.toJSONString(res));
    }
}
