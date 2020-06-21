package com.mistra.jmt.core.scan;

import com.mistra.jmt.core.JMTBean;
import com.mistra.jmt.core.JMTThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/21 21:53
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Slf4j
@Component
public class HelloServiceInjectProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetCls = bean.getClass();
        JMTBean annotation = targetCls.getAnnotation(JMTBean.class);
        if (annotation != null) {
            Field[] targetFld = targetCls.getDeclaredFields();
            for (Field field : targetFld) {
                JMTThreadPool threadPool = field.getAnnotation(JMTThreadPool.class);
                if (threadPool != null) {
                    log.info("=----------------------{}", threadPool.threadPoolName());
                    try {
                        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) field.get(targetCls);
                        log.info("-------------------{}", threadPoolExecutor.toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return bean;
    }
}
