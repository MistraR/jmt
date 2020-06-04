package com.mistra.jmt.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/4 23:20
 * @ Description: 用来测试的，模拟实际项目中的使用
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Slf4j
@Component
public class JobQueueTestLauncher {

    public static final ConcurrentLinkedQueue<String> queueString = new ConcurrentLinkedQueue<>();

    public static final ConcurrentLinkedQueue<TestModel> queueTestModel = new ConcurrentLinkedQueue<>();

    @PostConstruct
    private void init() {
        addQueueStringElement(1000);
        addQueueTestModelElement(10000);
    }

    /**
     * 往字符串队列添加元素
     *
     * @param number 添加对象数量
     */
    private void addQueueStringElement(int number) {
        for (int i = 0; i < number; i++) {
            queueString.add(number + "JVM 监控 调优 服务" + LocalDateTime.now());
        }
        log.info("添加元素之后队列大小：{}", queueString.size());
    }

    /**
     * 往测试对象队列添加元素
     *
     * @param number 添加对象数量
     */
    private void addQueueTestModelElement(int number) {
        for (int i = 0; i < number; i++) {
            queueTestModel.add(TestModel.builder()
                    .name("")
                    .age(new Random(99).nextInt())
                    .build());
        }
        log.info("添加元素之后队列大小：{}", queueTestModel.size());
    }
}
