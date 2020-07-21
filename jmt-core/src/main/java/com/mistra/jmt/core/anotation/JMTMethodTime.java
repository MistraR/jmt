package com.mistra.jmt.core.anotation;

import java.lang.annotation.*;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/7/21 22:37
 * @ Description: 统计方法执行耗时
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JMTMethodTime {

    String value() default "";
}
