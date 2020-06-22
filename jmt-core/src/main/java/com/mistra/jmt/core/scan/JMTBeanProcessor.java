package com.mistra.jmt.core.scan;

import com.mistra.jmt.core.anotation.JMTBean;
import com.mistra.jmt.core.anotation.JMTQueue;
import com.mistra.jmt.core.anotation.JMTThreadPool;
import com.mistra.jmt.core.ThreadPoolWarden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Component
public class JMTBeanProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(JMTBeanProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetCls = bean.getClass();
        JMTBean annotation = targetCls.getAnnotation(JMTBean.class);
        if (annotation != null) {
            try {
                Field[] declaredFields = targetCls.getDeclaredFields();
                for (Field field : declaredFields) {
                    JMTThreadPool threadPool = field.getAnnotation(JMTThreadPool.class);
                    if (threadPool != null) {
                        if (ThreadPoolWarden.getThreadPoolKeeper().containsKey(threadPool.threadPoolName())) {
                            throw new IllegalArgumentException("Scanner JMT bean Failure! ThreadPoolName duplicated!");
                        }
                        ThreadPoolWarden.getThreadPoolKeeper().put(threadPool.threadPoolName(), (ThreadPoolExecutor) field.get(targetCls));
                        continue;
                    }
                    JMTQueue queue = field.getAnnotation(JMTQueue.class);
                    if (queue != null) {
                        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) field.get(targetCls);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("Scanner JMT bean Failure!");
                e.printStackTrace();
            }
        }
        return bean;
    }
}
