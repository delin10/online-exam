package nil.ed.onlineexam.aop;

import lombok.extern.slf4j.Slf4j;
import nil.ed.onlineexam.util.AspectJUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
@Order(value = 0)
public class MethodInvocationLogAop {

    @Pointcut("@annotation(nil.ed.onlineexam.aop.annotation.MethodInvokeLog)")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void beforeMethod(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        // 获取签名参数名
        String paramInfos = AspectJUtils.getParametersAndValuesMap(joinPoint).entrySet().stream()
                .map(entry -> MessageFormat.format("{0} = {1}", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(","));
        String defClass = AspectJUtils.getMethodDefineClass(joinPoint).getSimpleName();
        String fullPathMethodName = MessageFormat.format("{0}.{1}", defClass, signature.getName());
        log.info("===> Start to invoke {}, entry params: {}", fullPathMethodName, paramInfos);
    }

    @AfterReturning(value = "pointcut()", returning = "returnResult")
    public void afterMethod(JoinPoint joinPoint, Object returnResult) {
        Signature signature = joinPoint.getSignature();
        String defClass = AspectJUtils.getMethodDefineClass(joinPoint).getSimpleName();
        String fullPathMethodName = MessageFormat.format("{0}.{1}", defClass, signature.getName());
        log.info("<=== After invoke {}, return: {}", fullPathMethodName, returnResult);
    }


    /**
     * 捕获异常之后重复抛出
     *
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void throwingMethod(JoinPoint joinPoint, Exception ex) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String defClass = AspectJUtils.getMethodDefineClass(joinPoint).getSimpleName();
        String fullPathMethodName = MessageFormat.format("{0}.{1}", defClass, signature.getName());
        log.error("<=== After invoke {}, throw Exception: ", fullPathMethodName, ex);
    }
}
