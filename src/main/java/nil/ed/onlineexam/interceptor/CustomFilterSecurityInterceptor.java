package nil.ed.onlineexam.interceptor;

import lombok.extern.slf4j.Slf4j;
import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import nil.ed.onlineexam.security.InvocationSecurityMetadataSourceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
    private SecurityMetadataSource invocationSecurityMetadataSourceService;

    public CustomFilterSecurityInterceptor(SecurityMetadataSource invocationSecurityMetadataSourceService) {
        this.invocationSecurityMetadataSourceService = invocationSecurityMetadataSourceService;
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return invocationSecurityMetadataSourceService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation invocation = new FilterInvocation(request,response,chain);
        invoke(invocation);
    }

    @Override
    public void destroy() {

    }

    @MethodInvokeLog
    public void invoke(FilterInvocation invocation)throws ServletException, IOException{
        InterceptorStatusToken token = super.beforeInvocation(invocation);

        try{
            invocation.getChain().doFilter(invocation.getRequest(), invocation.getResponse());
        }finally {
            super.afterInvocation(token, null);
        }
    }
}
