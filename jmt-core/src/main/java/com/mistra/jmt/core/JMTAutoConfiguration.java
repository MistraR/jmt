package com.mistra.jmt.core;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/1 22:18
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@ComponentScan(basePackages = {"com.mistra.jmt"})
@EntityScan("com.mistra.jmt.model")
@EnableScheduling
@Configuration
public class JMTAutoConfiguration {

    /**
     * 估计计算的样本大小
     */
    private int capacity;
}
