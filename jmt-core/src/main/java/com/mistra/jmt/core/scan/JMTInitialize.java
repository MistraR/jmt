package com.mistra.jmt.core.scan;

import com.mistra.jmt.core.JMTBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/18 22:46
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Component
public class JMTInitialize implements CommandLineRunner {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Autowired
    private ApplicationContext applicationContext;


    private static final String RESOURCE_PATTERN = "com.xxx";


    @Override
    public void run(String... args) throws Exception {
// 使用自定义扫描类，针对@Model进行扫描
        JMTAnnotationScanner scanner = JMTAnnotationScanner.getScanner((BeanDefinitionRegistry) beanFactory, JMTBean.class);
        scanner.doScan(RESOURCE_PATTERN).forEach(beanDefinitionHolder -> {
            Object o = applicationContext.getBean(beanDefinitionHolder.getBeanName());
            Class<?> clazz = o.getClass();
            JMTBean model = clazz.getAnnotation(JMTBean.class);
            String newName = model.tableName();
            Map<Object, Object> map = new HashMap<>();
            if (!StringUtils.isEmpty(newName)) {
//自己业务逻辑产生的结果Map(我这里是获取表的注释)
                map = xxxservice.findComments(newName);
            }
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        ModelProperty modelProperty = field.getAnnotation(ModelProperty.class);
                        if (modelProperty != null && !StringUtils.isEmpty(modelProperty.value())) {
                            map.put(field.getName(), modelProperty.value());
                        }
                    }

                } catch (Exception e) {
                    //doNothing
                }
            }
            //重新注入
            if (StringUtils.isEmpty(newName)) {
                newName = StringUtils.underscoreName(beanDefinitionHolder.getBeanName());
            }
            ((BeanDefinitionRegistry) beanFactory).removeBeanDefinition(beanDefinitionHolder.getBeanName());
            GenericBeanDefinition beanDef = new GenericBeanDefinition();
            beanDef.setBeanClass(Map.class);
            beanDef.setPropertyValues(new MutablePropertyValues(map));
            ((BeanDefinitionRegistry) beanFactory).registerBeanDefinition(newName, beanDef);
        });
    }
}
