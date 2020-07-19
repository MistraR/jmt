package com.mistra.jmt.core;

import com.mistra.jmt.model.ThreadPoolMemoryDump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
@Component
public class JMTWarden {

    private static final Logger log = LoggerFactory.getLogger(JMTWarden.class);

    /**
     * 线程池监控，String-线程池名称 ThreadPoolExecutor-线程池引用
     * 需要监控的线程池就放到这个缓存中
     */
    private static final ConcurrentHashMap<String, ThreadPoolExecutor> threadPoolKeeper = new ConcurrentHashMap<>();

    /**
     * 任务队列监控，String-队列名称 ？- 集合
     */
    private static final ConcurrentHashMap<String, Collection<Object>> collectionKeeper = new ConcurrentHashMap<>();

    /**
     * 任务队列监控，String-队列名称 ？- 映射
     */
    private static final ConcurrentHashMap<String, Map<Object, Object>> mapKeeper = new ConcurrentHashMap<>();

    /**
     * 全局对象监控，String-对象名称,Object-对象
     */
    private static final ConcurrentHashMap<String, Object> objectKeeper = new ConcurrentHashMap<>();

    /**
     * 缓存监控对象的实时内存占用大小 String - 对象名 Long - 对象占用内存大小 KB
     */
    public static final HashMap<String, Long> objectMemorySizeMap = new HashMap<>(32);

    /**
     * 缓存监控线程池的实时内存占用大小 String - 线程池名
     */
    public static final HashMap<String, ThreadPoolMemoryDump> threadPoolMemorySizeMap = new HashMap<>(16);

    /**
     * 添加线程池到监控缓存
     *
     * @param threadPoolName     线程池名称
     * @param threadPoolExecutor 线程池引用
     */
    public static void addExecutorService(String threadPoolName, ThreadPoolExecutor threadPoolExecutor) {
        if (threadPoolKeeper.containsKey(threadPoolName)) {
            throw new RuntimeException("This thread pool name is duplicated!");
        }
        if (threadPoolExecutor.getRejectedExecutionHandler() == null) {
            throw new RuntimeException("There is no reject policy for this thread pool!");
        }
        threadPoolKeeper.put(threadPoolName, threadPoolExecutor);
    }

    /**
     * 根据线程池名称获取线程池的引用
     *
     * @param threadPoolName 线程池名称
     * @return 线程池引用
     */
    public static ThreadPoolExecutor getThreadPoolExecutor(String threadPoolName) {
        if (!threadPoolKeeper.containsKey(threadPoolName)) {
            log.error("The thread pool does not exist!");
            return null;
        }
        return threadPoolKeeper.get(threadPoolName);
    }

    public static ConcurrentHashMap<String, ThreadPoolExecutor> getThreadPoolKeeper() {
        return threadPoolKeeper;
    }

    public static ConcurrentHashMap<String, Collection<Object>> getCollectionKeeper() {
        return collectionKeeper;
    }

    public static ConcurrentHashMap<String, Map<Object, Object>> getMapKeeper() {
        return mapKeeper;
    }

    public static ConcurrentHashMap<String, Object> getObjectKeeper() {
        return objectKeeper;
    }


}
