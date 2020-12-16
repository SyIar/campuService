package cn.edu.fudan.campuservice.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface ParamCheck {
    boolean notNull() default true;
}
