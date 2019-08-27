package nil.ed.book.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class AspectJUtils {
    /**
     * 获取定义方法的类
     * @param joinPoint 切点
     * @return
     */
    public static Class<?> getMethodDefineClass(JoinPoint joinPoint) {
        /**
         * getCanonicalName
         * getSimpleName
         * getTypeName
         * getName
         */
        return joinPoint.getTarget().getClass();
    }

    /**
     * 获得方法签名参数名，顺序为定义顺序
     * @param joinPoint MethodSignature类或者子类型
     * @return
     */
    public static String[] getParameterNames(JoinPoint joinPoint) {
        if (Objects.isNull(joinPoint) && !MethodSignature.class.isAssignableFrom(joinPoint.getClass())) {
            throw new IllegalArgumentException("Invalid joinPoint instance is:" + joinPoint);
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getParameterNames();
    }

    /**
     * 获得方法签名参数名和参数值的Map
     * @param joinPoint
     * @return HashMap类型
     */
    public static Map<String, Object> getParametersAndValuesMap(JoinPoint joinPoint) {
        String[] parameterNames = getParameterNames(joinPoint);
        Object[] parameterValues = joinPoint.getArgs();
        Map<String, Object> parametersAndValuesMap = new HashMap<>();
        IntStream.range(0, parameterNames.length).forEach(i -> {
            parametersAndValuesMap.putIfAbsent(parameterNames[i], parameterValues[i]);
        });
        return parametersAndValuesMap;
    }
}
