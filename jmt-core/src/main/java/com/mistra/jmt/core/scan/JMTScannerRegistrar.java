package com.mistra.jmt.core.scan;

import com.mistra.jmt.core.anotation.EnableJMT;
import com.mistra.jmt.core.anotation.JMTBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/18 23:23
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
public class JMTScannerRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String basePackages = "";

    @Override
    @PostConstruct
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableJMT.class.getName()));
        if (annotationAttributes != null) {
            // 扫描@JMTBean
            JMTAnnotationScanner scanner = JMTAnnotationScanner.getScanner(registry, JMTBean.class);
            List<String> basePackages = new ArrayList<String>();
            basePackages.addAll(
                    Arrays.stream(annotationAttributes.getStringArray("basePackages"))
                            .filter(StringUtils::hasText)
                            .collect(Collectors.toList()));
            scanner.registerDefaultFilters();
            scanner.doScan(StringUtils.toStringArray(basePackages));
        }
    }

}
