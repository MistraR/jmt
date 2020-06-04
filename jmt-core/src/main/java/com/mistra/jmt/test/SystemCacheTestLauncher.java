package com.mistra.jmt.test;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
public class SystemCacheTestLauncher {

    public static final ConcurrentHashMap<String, TestModel> testModelConcurrentHashMap = new ConcurrentHashMap<>();

    public static final CopyOnWriteArrayList<TestModel> testModelCopyOnWriteArrayList = new CopyOnWriteArrayList<>();

    @PostConstruct
    private void init() {
        addTestModelConcurrentHashMap(1000);
        addTestModelCopyOnWriteArrayList(1000);
    }

    /**
     * 不断往缓存中添加元素
     */
    @Scheduled(fixedDelay = 10000)
    private void execute() {
        addTestModelConcurrentHashMap(100);
        addTestModelCopyOnWriteArrayList(100);
    }

    private void addTestModelConcurrentHashMap(int number) {

    }

    private void addTestModelCopyOnWriteArrayList(int number) {

    }
}
