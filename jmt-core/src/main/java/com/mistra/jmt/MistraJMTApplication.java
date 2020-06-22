package com.mistra.jmt;

import com.mistra.jmt.core.anotation.EnableJMT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/5/31 20:50
 * @ Description: JVM monitoring and tuning
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@EnableJMT(basePackages = "com.mistra.jmt.test")
@SpringBootApplication
public class MistraJMTApplication {

    public static void main(String[] args) {
        SpringApplication.run(MistraJMTApplication.class, args);
    }
}
