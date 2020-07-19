package com.mistra.jmt.test;

import com.mistra.jmt.core.anotation.JMTBean;
import com.mistra.jmt.core.anotation.JMTCollection;
import com.mistra.jmt.core.anotation.JMTMap;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
@JMTBean
public class SystemCacheTestLauncher {

    @JMTMap(mapName = "testModelConcurrentHashMap")
    public static final ConcurrentHashMap<String, TestModel> testModelConcurrentHashMap = new ConcurrentHashMap<>();

    @JMTCollection(collectionName = "testModelCopyOnWriteArrayList")
    public static final CopyOnWriteArrayList<TestModel> testModelCopyOnWriteArrayList = new CopyOnWriteArrayList<>();

    @PostConstruct
    private void init() {
        addTestModelConcurrentHashMap(new Random().nextInt(50) + 50);
        addTestModelCopyOnWriteArrayList(new Random().nextInt(50) + 50);
    }

    /**
     * 不断往缓存中添加元素
     */
    @Scheduled(fixedDelay = 30000)
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
