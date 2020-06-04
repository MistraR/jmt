package com.mistra.jmt.test;

import cn.hutool.core.thread.NamedThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
@Component
public class ThreadPoolTestLauncher {

    public static final ThreadPoolExecutor threadPoolExecutorA = new ThreadPoolExecutor(5, 10, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000), new NamedThreadFactory("测试线程池A-", false));

    public static final ThreadPoolExecutor threadPoolExecutorB = new ThreadPoolExecutor(10, 20, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10000), new NamedThreadFactory("测试线程池B-", false));

    @PostConstruct
    private void init() {
        for (int i = 0; i < 1015; i++) {
            threadPoolExecutorA.submit(new TestTaskA());
        }
        for (int i = 0; i < 10030; i++) {
            threadPoolExecutorB.submit(new TestTaskA());
        }
    }

    private static class TestTaskA implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(30000);
                System.out.println("线程名称： " + Thread.currentThread().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
