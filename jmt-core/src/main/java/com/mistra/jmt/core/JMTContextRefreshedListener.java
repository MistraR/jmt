package com.mistra.jmt.core;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JMTContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 根容器为Spring容器
        if (event.getApplicationContext().getParent() == null) {
            Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(JMTBean.class);
            for (Object bean : beans.values()) {
                System.err.println(bean == null ? "null" : bean.getClass().getName());
                bean.getClass().getFields();
            }
            System.err.println("=====ContextRefreshedEvent=====" + event.getSource().getClass().getName());
        }
    }
}
