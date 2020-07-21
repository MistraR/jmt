package com.mistra.jmt.test;


import com.mistra.jmt.core.JMTMemoryEstimate;
import com.mistra.jmt.core.anotation.JMTMethodTime;
import org.apache.lucene.util.RamUsageEstimator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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

    private List<TestModel> createListData(int num) {
        List<TestModel> testModelList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            testModelList.add(new TestModel("AHGFIQJTOVFLASJDFLJA:LFJLOASDJGAG)(+?~!Mistra" + i, i));
        }
        return testModelList;
    }

    private ConcurrentHashMap<String, TestModel> createMapData(int num) {
        ConcurrentHashMap<String, TestModel> testModelList = new ConcurrentHashMap<>(20000001, 1);
        for (int i = 0; i < num; i++) {
            testModelList.put(i + "", new TestModel("Mistra" + i, i));
        }
        return testModelList;
    }

    @GetMapping("/testMemory/{num}")
    public String testMemory(@PathVariable("num") int num) throws InterruptedException {
        List<TestModel> testModelList = createListData(num);
        System.out.println("testMemory1---------------------------------------------------------------------");
        long a = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(JMTMemoryEstimate.estimateCollection(testModelList))));
        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - a));
        return "success";
    }

    @GetMapping("/testMemory2/{num}")
    public String testMemory2(@PathVariable("num") int num) throws InterruptedException {
        List<TestModel> testModelList = createListData(num);
        System.out.println("testMemory2---------------------------------------------------------------------");
        long a = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(RamUsageEstimator.sizeOfCollection(testModelList))));
        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - a));
        return "success";
    }

    @GetMapping("/testMemory3/{num}")
    public String testMemory3(@PathVariable("num") int num) throws InterruptedException {
        List<TestModel> testModelList = createListData(num);
        System.out.println("testMemory3---------------------------------------------------------------------");
        long a = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(JMTMemoryEstimate.jmtSizeOfObject(testModelList))));
        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - a));
        return "success";
    }

    @GetMapping("/testMemory4/{num}")
    public String testMemory4(@PathVariable("num") int num) throws InterruptedException {
        List<TestModel> testModelList = createListData(num);
        System.out.println("testMemory4---------------------------------------------------------------------");
        long a = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(RamUsageEstimator.sizeOfObject(testModelList))));
        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - a));
        return "success";
    }


    @JMTMethodTime
    @GetMapping("/testMemory5/{num}")
    public String testMemory5(@PathVariable("num") int num) throws InterruptedException {
        ConcurrentHashMap<String, TestModel> testModelList = createMapData(num);
        System.out.println("testMemory5---------------------------------------------------------------------");
        long a = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(RamUsageEstimator.sizeOfObject(testModelList))));
        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - a));

        long b = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(RamUsageEstimator.sizeOfMap(testModelList))));
        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - b));
        return "success";
    }

    @GetMapping("/testMemory6/{num}")
    public String testMemory6(@PathVariable("num") int num) throws InterruptedException {
        ConcurrentHashMap<String, TestModel> testModelList = createMapData(num);
        System.out.println("testMemory6---------------------------------------------------------------------");
//        long a = System.currentTimeMillis();
//        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(JMTMemoryEstimate.jmtSizeOfObject(testModelList))));
//        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - a));
//
//
//        long b = System.currentTimeMillis();
//        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(JMTMemoryEstimate.estimateMap(testModelList))));
//        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - b));
        return "success";
    }

    @GetMapping("/testMemory7/{num}")
    public String testMemory7(@PathVariable("num") int num) throws InterruptedException {
        ConcurrentHashMap<String, TestModel> testModelList = createMapData(num);
        System.out.println("testMemory7---------------------------------------------------------------------");
        long a = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(JMTMemoryEstimate.estimateMap(testModelList))));
        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - a));
//
//
//        long b = System.currentTimeMillis();
//        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(JMTMemoryEstimate.estimateMap(testModelList))));
//        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - b));
        return "success";
    }

    @GetMapping("/testMemory8/{num}")
    public String testMemory8(@PathVariable("num") int num) throws InterruptedException {
        ConcurrentHashMap<String, TestModel> testModelList = createMapData(num);
        System.out.println("testMemory8---------------------------------------------------------------------");
        long b = System.currentTimeMillis();
        System.out.println("testModelList集合占用内存大小(估计计算)：" + (JMTMemoryEstimate.unitsMB(RamUsageEstimator.sizeOfObject(testModelList))));
        System.out.println("testModelList估计计算花费时间：" + (System.currentTimeMillis() - b));
        return "success";
    }

}
