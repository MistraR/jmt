package com.mistra.jmt.test;

import cn.hutool.core.thread.NamedThreadFactory;
import com.mistra.jmt.core.anotation.JMTBean;
import com.mistra.jmt.core.anotation.JMTThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
@JMTBean
public class ThreadPoolTestLauncher {

    @JMTThreadPool(threadPoolName = "threadPoolExecutorA")
    public static final ThreadPoolExecutor threadPoolExecutorA = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(10000), new NamedThreadFactory("测试线程池A-", false), new JMTRejectedExecutionHandler());

    @JMTThreadPool(threadPoolName = "threadPoolExecutorB")
    public static final ThreadPoolExecutor threadPoolExecutorB = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(10000), new NamedThreadFactory("测试线程池B-", false), new JMTRejectedExecutionHandler());

    @PostConstruct
    private void init() {
        for (int i = 0; i < new Random().nextInt(50) + 50; i++) {
            threadPoolExecutorA.submit(new TestTaskA());
        }
        for (int i = 0; i < new Random().nextInt(50) + 50; i++) {
            threadPoolExecutorB.submit(new TestTaskA());
        }
    }

    /**
     * 不断往线程池添加任务
     */
    @Scheduled(fixedDelay = 30000)
    private void execute() {
        init();
    }

    private static class TestTaskA implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                log.debug("线程名称： {}", Thread.currentThread().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
