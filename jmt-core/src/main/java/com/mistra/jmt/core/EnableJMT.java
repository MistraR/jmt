package com.mistra.jmt.core;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/1 22:16
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Documented
@Import({JMTAutoConfiguration.class})
public @interface EnableJMT {
    String[] basePackages();
}
