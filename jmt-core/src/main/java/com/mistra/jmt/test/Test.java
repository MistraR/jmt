package com.mistra.jmt.test;

import com.mistra.jmt.core.RamUsageEstimator;
import org.openjdk.jol.info.ClassLayout;

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
//        System.out.println(ClassLayout.parseClass(TestModel.class).toPrintable());
        TestModel testModel = new TestModel("abcdefgh", 22);
//        ClassLayout classLayout = ClassLayout.parseInstance(testModel);
//        System.out.println(classLayout.toPrintable());
//        System.out.println("-----------------String------------------");
//        System.out.println(ClassLayout.parseClass(String.class).toPrintable());
        String a = "abcdefgh";
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
        // 一个字符串的大小=字符串本身大小+字符数组的大小
        System.out.println(ClassLayout.parseInstance(a.toCharArray()).toPrintable());

        System.out.println("字符串a占用内存大小：" + RamUsageEstimator.sizeOf(a));
        System.out.println("TestModel占用内存大小：" + RamUsageEstimator.sizeOf(testModel));
    }
}
