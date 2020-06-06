package com.mistra.jmt.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.misc.Unsafe;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/6 10:45
 * @ Description: Unsafe工具，内存偏移量计算
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Slf4j
@Component
public class UnsafeOffsetEstimator {

    /**
     * Unsafe
     */
    private static final Unsafe unsafe = Unsafe.getUnsafe();

    public static long get(){
        return 1;
    }

}
