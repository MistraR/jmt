package com.mistra.jmt.core.anotation;

import java.lang.annotation.*;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/22 22:02
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Documented
public @interface JMTObject {
    String objectName();
}
