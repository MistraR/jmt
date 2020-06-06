package com.mistra.jmt.test;

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
//        List<Animal> animalList = new ArrayList<>(20000000);
//        for (int i = 0; i < 20000000; i++) {
//            animalList.add(new Animal( 22));
//        }
//        System.out.println("animalList集合占用内存大小：" + RamUsageEstimator.humanSizeOf(animalList));
//        System.out.println(ClassLayout.parseClass(TestModel.class).toPrintable());

        TestModel testModel1 = new TestModel( 22);
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
        System.out.println("字数组b占用内存大小：" + RamUsageEstimator.humanSizeOf(b));
        System.out.println("TestModel占用内存大小：" + RamUsageEstimator.humanSizeOf(testModel));

        List<TestModel> testModelList = new ArrayList<>(20000000);
        for (int i = 0; i < 20000000; i++) {
            testModelList.add(new TestModel("abcdefg", 22));
        }
        System.out.println("testModelList集合占用内存大小：" + RamUsageEstimator.humanSizeOf(testModelList));
//        System.out.println(ClassLayout.parseInstance(testModelList).toPrintable());
//        Object[] object = new Object[10000];
//        for (int i = 0; i < 10000; i++) {
//            object[i] = null;
//        }
//        System.out.println("10000个空数组占用内存大小：" + RamUsageEstimator.sizeOf(object));
//        Object[] object1 = new Object[1];
//        for (int i = 0; i < 1; i++) {
//            object[i] = null;
//        }
//        System.out.println("1个空数组占用内存大小：" + RamUsageEstimator.sizeOf(object1));
//        Object[] object21 = new Object[2];
//        for (int i = 0; i < 2; i++) {
//            object[i] = null;
//        }
//        System.out.println("2个空数组占用内存大小：" + RamUsageEstimator.sizeOf(object21));
//        Object[] object3 = new Object[3];
//        for (int i = 0; i < 3; i++) {
//            object[i] = null;
//        }
//        System.out.println("3个空数组占用内存大小：" + RamUsageEstimator.sizeOf(object3));
//
//        Object[] object4 = new Object[4];
//        for (int i = 0; i < 4; i++) {
//            object[i] = null;
//        }
//        System.out.println("4个空数组占用内存大小：" + RamUsageEstimator.sizeOf(object4));
    }

    static class Animal{
        private int age;

        public Animal(int age) {
            this.age = age;
        }
    }
}
