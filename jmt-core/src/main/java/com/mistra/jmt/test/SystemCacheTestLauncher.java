package com.mistra.jmt.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/4 23:20
 * @ Description: 用来测试的，模拟实际项目中缓存的使用
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
        addTestModelConcurrentHashMap(new Random(50).nextInt() + 50);
        addTestModelCopyOnWriteArrayList(new Random(50).nextInt() + 50);
    }

    /**
     * 不断往缓存中添加元素
     */
    @Scheduled(fixedDelay = 10000)
    private void execute() {
        init();
    }

    private void addTestModelConcurrentHashMap(int number) {
        String name = RandomStringUtils.randomAlphanumeric(20);
        testModelConcurrentHashMap.put(name, TestModel.builder()
                .name(name)
                .age(new Random(99).nextInt())
                .build());
    }

    private void addTestModelCopyOnWriteArrayList(int number) {
        for (int i = 0; i < number; i++) {
            testModelCopyOnWriteArrayList.add(
                    TestModel.builder()
                            .name(RandomStringUtils.randomAlphanumeric(20))
                            .age(new Random(99).nextInt())
                            .build());
        }
    }
}
