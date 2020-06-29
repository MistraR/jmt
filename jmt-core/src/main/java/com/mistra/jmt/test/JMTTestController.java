package com.mistra.jmt.test;


import com.mistra.jmt.core.JMTMemoryEstimate;
import com.mistra.jmt.model.ThreadPoolMemoryDump;
import org.apache.lucene.util.RamUsageEstimator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/4 23:20
 * @ Description: 用来测试的，模拟实际项目中的使用
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@RestController
@RequestMapping("/jmt/test")
public class JMTTestController {

    @Autowired
    private JobQueueTestLauncher jobQueueTestLauncher;

    private ConcurrentLinkedQueue<TestModel> createQueueData(int num) {
        ConcurrentLinkedQueue<TestModel> testModelList = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < num; i++) {
            testModelList.add(new TestModel("AHGFIQJTOVFLASJDFLJA:LFJLOASDJGAG)(+?~!Mistra" + i, i));
        }
        return testModelList;
    }

    private List<TestModel> createData() {
        List<TestModel> testModelList = new ArrayList<>(10000000);
        for (int i = 0; i < 10000000; i++) {
            testModelList.add(new TestModel("AHGFIQJTOVFLASJDFLJA:LFJLOASDJGAG)(+?~!Mistra" + i, i));
        }
        return testModelList;
    }

    private List<TestModel> createData(int num) {
        List<TestModel> testModelList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            testModelList.add(new TestModel("AHGFIQJTOVFLASJDFLJA:LFJLOASDJGAG)(+?~!Mistra" + i, i));
        }
        return testModelList;
    }

    private ConcurrentHashMap<String, TestModel> createMapData() {
        ConcurrentHashMap<String, TestModel> testModelList = new ConcurrentHashMap<>(20000001, 1);
        for (int i = 0; i < 20000000; i++) {
            testModelList.put(i + "", new TestModel("Mistra" + i, i));
        }
        return testModelList;
    }

    @GetMapping("/getThreadPoolExecutorInfo")
    public ThreadPoolMemoryDump getThreadPoolExecutorInfo(String name) {
        return null;
    }

    @GetMapping("/testMemory/{num}")
    public String testMemory(@PathVariable("num") int num) throws InterruptedException {
        List<TestModel> testModelList = createData(num);
        System.out.println("testMemory2---------------------------------------------------------------------");
        long a = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小(递归计算)：" + (JMTMemoryEstimate.unitsMB(JMTMemoryEstimate.estimateCollection(testModelList))));
        System.out.println("testModelList递归计算花费时间：" + (System.currentTimeMillis() - a));
        long b = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小（估算）：" + JMTMemoryEstimate.unitsMB(Estimator(testModelList).longValue()));
        System.out.println("testModelList估算计算花费时间：" + (System.currentTimeMillis() - b));
        System.out.println("testMemory2---------------------------------------------------------------------");
//        System.out.println("testModelList:" + com.mistra.jmt.core.RamUsageEstimator.sizeOf(testModelList));
        System.out.println("testModelList:shallowSizeOf " + com.mistra.jmt.core.RamUsageEstimator.shallowSizeOf(testModelList));
        System.out.println("testModelList:shallowSizeOf " + com.mistra.jmt.core.RamUsageEstimator.shallowSizeOf(testModelList.toArray()));
        System.out.println("testModelList:shallowSizeOf " + JMTMemoryEstimate.shallowSizeOf(testModelList));
        System.out.println("testModelList:shallowSizeOf " + JMTMemoryEstimate.shallowSizeOf(testModelList.toArray()));
        return "success";
    }

    public static BigDecimal EstimatorQueue(ConcurrentLinkedQueue<TestModel> queues) {
        int size = queues.size();
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            longList.add(com.mistra.jmt.core.RamUsageEstimator.sizeOf(queues.poll()));
        }
        Double collect = longList.stream().collect(Collectors.averagingDouble(Long::longValue));
        BigDecimal a = new BigDecimal(collect).multiply(new BigDecimal(size));
        BigDecimal b = new BigDecimal(32).multiply(new BigDecimal(size));
        return a.add(b);
    }

    public static BigDecimal Estimator(List<TestModel> testModelList) {
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            longList.add(com.mistra.jmt.core.RamUsageEstimator.sizeOf(testModelList.get(i)));
        }
        Double collect = longList.stream().collect(Collectors.averagingDouble(Long::longValue));
        BigDecimal a = new BigDecimal(collect).multiply(new BigDecimal(testModelList.size()));
        BigDecimal b = new BigDecimal(JMTMemoryEstimate.shallowSizeOf(testModelList.get(0))).multiply(new BigDecimal(testModelList.size()));
        BigDecimal c = new BigDecimal(JMTMemoryEstimate.shallowSizeOf(testModelList));
        return a.add(b).add(c);
    }

    @GetMapping("/testMemory2/{num}")
    public String testMemory2(@PathVariable("num") int num) throws InterruptedException {
        ConcurrentLinkedQueue<TestModel> testModelList = createQueueData(num);
        System.out.println("testMemory2---------------------------------------------------------------------");
        long a = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小(递归计算)：" + (JMTMemoryEstimate.unitsMB(JMTMemoryEstimate.estimateCollection(testModelList))));
        System.out.println("testModelList递归计算花费时间：" + (System.currentTimeMillis() - a));
        long b = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小（估算）：" + JMTMemoryEstimate.unitsMB(JMTMemoryEstimate.jmtSizeOfObject(testModelList)));
        System.out.println("testModelList估算计算花费时间：" + (System.currentTimeMillis() - b));
        System.out.println("testMemory2---------------------------------------------------------------------");
        return "success";
    }

    public static BigDecimal estimatorMap(ConcurrentHashMap<? extends Object, ? extends Object> concurrentHashMap) {
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            longList.add(RamUsageEstimator.sizeOfObject(concurrentHashMap.get(i)));
        }
        Double collect = longList.stream().collect(Collectors.averagingDouble(Long::longValue));
        BigDecimal a = new BigDecimal(collect).multiply(new BigDecimal(concurrentHashMap.size()));
        BigDecimal b = new BigDecimal(32).multiply(new BigDecimal(concurrentHashMap.size()));
        return a.add(b);
    }

}
