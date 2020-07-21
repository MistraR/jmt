package com.mistra.jmt.core.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/7/21 22:23
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Aspect
@Component
public class MethodTimeInterceptor {

    private static Log logger = LogFactory.getLog(MethodTimeInterceptor.class);

    private static final long ONE_MINUTE = 1000;

    // service层的统计耗时切面，类型必须为final String类型的,注解里要使用的变量只能是静态常量类型的
    public static final String POINT = "execution (* com.blinkfox.test.service.impl.*.*(..))";

    @Pointcut("execution(* com.mistra.jmt.*.*(..))")
    public void pointcut() {

    }

    @Around("@annotation(com.mistra.jmt.core.anotation.JMTMethodTime)")
    public Object getMethodExecuteTimeForLogger(ProceedingJoinPoint point) throws Throwable {
        System.out.println("---------------getMethodExecuteTime------------------");
        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        System.out.println("executeTime=" + executeTime + "------------------");
        return result;
    }
}
