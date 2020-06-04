package com.mistra.jmt.test;

import org.openjdk.jol.info.ClassLayout;

public class Test {

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(TestModel.class).toPrintable());
        TestModel testModel = new TestModel("AAA", 22);
        ClassLayout classLayout=ClassLayout.parseInstance(testModel);
        System.out.println(classLayout.toPrintable());
        System.out.println("-----------------String------------------");
        System.out.println(ClassLayout.parseClass(String.class).toPrintable());
        String a = "abcdefg";
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
        // 一个字符串的大小=字符串本身大小+字符数组的大小
        System.out.println(ClassLayout.parseInstance(a.toCharArray()).toPrintable());
    }
}
