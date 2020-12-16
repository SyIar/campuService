package cn.edu.fudan.campuservice.aspect;

import cn.edu.fudan.campuservice.annotation.ParamCheck;
import cn.edu.fudan.campuservice.exception.ParamsException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
@Aspect
public class ParamsCheckAspect {
    @Pointcut("execution(public * cn.edu.fudan.campuservice.controller.*.*(..))")
    public void checkParams() {

    }

    @Around("checkParams()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return joinPoint.proceed();
        }

        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            if (parameterAnnotations[i].length > 0) {
                for (int j = 0; j < parameterAnnotations[0].length; j++) {
                    if (parameterAnnotations[i][j] != null && parameterAnnotations[i][j] instanceof ParamCheck
                            && ((ParamCheck) parameterAnnotations[i][j]).notNull() && args[i] == null) {
                        throw new ParamsException(parameterNames[i], parameterTypes[i].getTypeName());
                    }
                }
            }

        }

        return joinPoint.proceed();
    }
}
