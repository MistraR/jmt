package com.mistra.jmt.test;

import cn.hutool.core.thread.NamedThreadFactory;
import com.mistra.jmt.core.ThreadPool;
import com.mistra.jmt.core.ThreadPoolWarden;
import lombok.extern.slf4j.Slf4j;
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
public class ThreadPoolTestLauncher {

    @ThreadPool(value = "threadPoolExecutorA")
    public static final ThreadPoolExecutor threadPoolExecutorA = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(10000), new NamedThreadFactory("测试线程池A-", false), new JMTRejectedExecutionHandler());

    public static final ThreadPoolExecutor threadPoolExecutorB = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(10000), new NamedThreadFactory("测试线程池B-", false), new JMTRejectedExecutionHandler());

    @PostConstruct
    private void init() {
        for (int i = 0; i < new Random(50).nextInt() + 50; i++) {
            threadPoolExecutorA.submit(new TestTaskA());
        }
        for (int i = 0; i < new Random(50).nextInt() + 50; i++) {
            threadPoolExecutorB.submit(new TestTaskA());
        }
        ThreadPoolWarden.addExecutorService("threadPoolExecutorA", threadPoolExecutorA);
        ThreadPoolWarden.addExecutorService("threadPoolExecutorB", threadPoolExecutorB);
    }

    /**
     * 不断往线程池添加任务
     */
//    @Scheduled(fixedDelay = 10000)
    private void execute() {
        init();
    }

    private static class TestTaskA implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                log.info("线程名称： {}", Thread.currentThread().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
