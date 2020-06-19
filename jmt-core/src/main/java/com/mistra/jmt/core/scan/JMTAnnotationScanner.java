package com.mistra.jmt.core.scan;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/18 23:02
 * @ Description: 自定义注解扫描器
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
public class JMTAnnotationScanner extends ClassPathBeanDefinitionScanner {

    public static List<Field> getFieldsOfAnnotation(Object cls, Class<? extends Annotation> anoClass) {
        return getFieldsOfAnnotation(cls.getClass(), anoClass);
    }

    public static List<Field> getFieldsOfAnnotation(Class<?> cls, Class<? extends Annotation> anoClass) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazz = cls; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields1 = clazz.getDeclaredFields();
                for (Field f : fields1) {
                    Annotation annotation = f.getAnnotation(anoClass);
                    if (annotation != null) {
                        f.setAccessible(true);
                        fields.add(f);
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 实体类对应的AnnotationClazz
     */
    private Class<? extends Annotation> selfAnnotationClazz;

    /**
     * 传值使用的临时静态变量
     */
    private static Class<? extends Annotation> staticTempAnnotationClazz = null;

    /**
     * 因构造函数无法传入指定的Annotation类，需使用静态方法来调用
     *
     * @param registry
     * @param clazz
     * @return
     */
    public static synchronized JMTAnnotationScanner getScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> clazz) {
        staticTempAnnotationClazz = clazz;
        JMTAnnotationScanner scanner = new JMTAnnotationScanner(registry);
        scanner.selfAnnotationClazz = clazz;
        return scanner;
    }

    JMTAnnotationScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    // 构造函数需调用函数，使用静态变量annotationClazz传值
    @Override
    public void registerDefaultFilters() {
        // 添加需扫描的Annotation Class
        this.addIncludeFilter(new AnnotationTypeFilter(staticTempAnnotationClazz));
    }

    // 以下为初始化后调用的方法
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition)
                && beanDefinition.getMetadata().hasAnnotation(this.selfAnnotationClazz.getName());

    }
}