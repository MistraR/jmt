package com.mistra.jmt.service;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/1 22:21
 * @ Description: 用于测试，往目标对象添加元素
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
public interface JMTService {

    void addThreadTask(int number);

    void addCollectionElement(int number);

    void addObject(int number);

    void addMapElement(int number);
}
