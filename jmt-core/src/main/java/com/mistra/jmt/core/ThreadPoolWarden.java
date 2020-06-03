package com.mistra.jmt.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/2 23:28
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
public class ThreadPoolWarden {

    public static final ConcurrentHashMap<String, ThreadPoolExecutor>  threadPool=new ConcurrentHashMap<>();
}
