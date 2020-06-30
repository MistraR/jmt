package com.mistra.jmt.test;

import com.mistra.jmt.core.JMTMemoryEstimate;
import com.mistra.jmt.core.RamUsageEstimator;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/2 23:24
 * @ Description: 用org.openjdk.jol.info包查看某个对象占用的内存大小
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
public class Test {

    public static void main(String[] args) throws IllegalAccessException {
//        C();
//        B();

        TestModel testModel = new TestModel("AHGFIQJTOVFLASJDFLJA:LFJLOASDJGAG)(+?~!Mistra" + 1111111, 1);
        System.out.println("testModel:" + JMTMemoryEstimate.jmtSizeOfObject(testModel));
        System.out.println("testModel:" + RamUsageEstimator.sizeOf(testModel));
        System.out.println("testModel:" + org.apache.lucene.util.RamUsageEstimator.sizeOfObject(testModel));
    }


    public static void C() {
        TestModel testModel = new TestModel("Mistra1", 22);
        TestModel testModel1 = new TestModel("Mistra1dsdlkfsdlkjflsjldfjsdlfjlsjf4444488888", 22);
        System.out.println("testModel:" + JMTMemoryEstimate.jmtSizeOfObject(testModel));
        System.out.println("testModel1:" + JMTMemoryEstimate.jmtSizeOfObject(testModel1));
    }

    public static void B() {
        TestModel testModel = new TestModel("Mistra1", 22);
        TestModel testModel1 = new TestModel("Mistra1dsdlkfsdlkjflsjldfjsdlfjlsjf4444488888", 22);
        System.out.println("testModel:" + RamUsageEstimator.sizeOf(testModel));
        System.out.println("testModel1:" + RamUsageEstimator.sizeOf(testModel1));
        System.out.println("testModel:" + org.apache.lucene.util.RamUsageEstimator.sizeOfObject(testModel));
        System.out.println("testModel1:" + org.apache.lucene.util.RamUsageEstimator.sizeOfObject(testModel1));
        String a = "abcdefg";
        char[] b = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        System.out.println("testModel1:" + org.apache.lucene.util.RamUsageEstimator.sizeOf(a));
        System.out.println("testModel1:" + org.apache.lucene.util.RamUsageEstimator.sizeOf(b));
    }

    public static void A() {
        TestModel testModel1 = new TestModel("Mistra", 22);
        System.out.println(ClassLayout.parseInstance(testModel1).toPrintable());
        TestModel testModel = new TestModel("abcdefg", 22);
        System.out.println(ClassLayout.parseInstance(testModel).toPrintable());
        ClassLayout classLayout = ClassLayout.parseInstance(testModel);
        System.out.println(classLayout.toPrintable());
        System.out.println("-----------------String------------------");
        System.out.println(ClassLayout.parseClass(String.class).toPrintable());
        String a = "abcdefg";
        char[] b = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
        // 一个字符串的大小=字符串本身大小+字符数组的大小
        System.out.println(ClassLayout.parseInstance(a.toCharArray()).toPrintable());
        System.out.println(ClassLayout.parseInstance(b).toPrintable());
        System.out.println("字符串a占用内存大小：" + RamUsageEstimator.humanSizeOf(a));
        System.out.println("数组b占用内存大小：" + RamUsageEstimator.humanSizeOf(b));
        System.out.println("TestModel占用内存大小：" + RamUsageEstimator.humanSizeOf(testModel));

        List<TestModel> testModelList = new ArrayList<>(20000000);
        for (int i = 0; i < 20000000; i++) {
            testModelList.add(new TestModel("abcdefg", 22));
        }
        System.out.println("testModelList集合占用内存大小：" + RamUsageEstimator.humanSizeOf(testModelList));
    }


}
