package com.mistra.jmt.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/4 23:20
 * @ Description: 用来测试的，模拟生产者消费者模式，监控待消费队列的内存大小
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Slf4j
@Component
public class JobQueueTestLauncher implements Runnable {

    public static final ConcurrentLinkedQueue<String> queueString = new ConcurrentLinkedQueue<>();

    public static final ConcurrentLinkedQueue<TestModel> queueTestModel = new ConcurrentLinkedQueue<>();

    @PostConstruct
    private void init() {
        addQueueStringElement(new Random(50).nextInt() + 50);
        addQueueTestModelElement(new Random(50).nextInt() + 50);
        new Thread(new JobQueueTestLauncher()).start();
    }

    /**
     * 不断往队列中生产元素
     */
//    @Scheduled(fixedDelay = 10000)
    private void execute() {
        init();
    }

    /**
     * 往字符串队列添加元素
     *
     * @param number 添加对象数量
     */
    private void addQueueStringElement(int number) {
        for (int i = 0; i < number; i++) {
            queueString.add(RandomStringUtils.randomAlphanumeric(20) + LocalDateTime.now());
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
                    .name(RandomStringUtils.randomAlphanumeric(20))
                    .age(new Random(99).nextInt())
                    .build());
        }
        log.info("添加元素之后队列大小：{}", queueTestModel.size());
    }

    /**
     * 单线程消费元素
     */
    @Override
    public void run() {
        while (true) {
            try {
                String s = queueString.poll();
                if (s != null) {
                    log.info("queueString 消费了：{}", s);
                }
                TestModel testModel = queueTestModel.poll();
                if (testModel != null) {
                    log.info("queueTestModel 消费了：{}", testModel);
                }
                Thread.sleep(400);
            } catch (Exception e) {
                log.error("消费元素出错！");
            }
        }
    }
}
