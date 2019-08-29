package nil.ed.onlineexam.aop.annotation;


import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodInvokeLog {
}
