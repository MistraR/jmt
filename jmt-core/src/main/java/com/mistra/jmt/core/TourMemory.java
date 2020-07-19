package com.mistra.jmt.core;

import cn.hutool.system.RuntimeInfo;
import cn.hutool.system.SystemUtil;
import com.mistra.jmt.core.scan.JMTBeanProcessor;
import com.mistra.jmt.dao.JVMMemoryLogRepository;
import com.mistra.jmt.dao.ObjectMemoryDumpRepository;
import com.mistra.jmt.dao.ThreadPoolMemoryDumpRepository;
import com.mistra.jmt.model.CommonObjectMemoryDump;
import com.mistra.jmt.model.JVMMemoryLog;
import com.mistra.jmt.model.ThreadPoolMemoryDump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/3 21:54
 * @ Description: 计算各个监控对象的内存占用情况，超过JMTAutoConfiguration.capacity大小的集合类数据将会采用估计计算的方式，精确计算有点耗时
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Component
public class TourMemory {

    private static final Logger log = LoggerFactory.getLogger(JMTBeanProcessor.class);

    @Autowired
    private ThreadPoolMemoryDumpRepository threadPoolMemoryDumpRepository;

    @Autowired
    private ObjectMemoryDumpRepository objectMemoryDumpRepository;

    @Autowired
    private JVMMemoryLogRepository jvmMemoryLogRepository;

    @Autowired
    private JMTAutoConfiguration jmtAutoConfiguration;

    @Scheduled(fixedDelay = 3000)
    private void tour() {
        tourThreadPools();
        tourObjects();
        tourJVM();
    }

    /**
     * 监控JVM指标
     */
    private void tourJVM() {
        if (jmtAutoConfiguration.isSaveData()) {
            JVMMemoryLog jvmMemoryLog = new JVMMemoryLog();
            jvmMemoryLog.setCreateDate(new Date());
            RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();
            jvmMemoryLog.setUsedMemory(runtimeInfo.getUsableMemory());
            jvmMemoryLog.setFreeMemory(runtimeInfo.getFreeMemory());
            jvmMemoryLog.setTotalMemory(runtimeInfo.getMaxMemory());
            jvmMemoryLog.setUsedRatio(runtimeInfo.getUsableMemory() / runtimeInfo.getMaxMemory());
            jvmMemoryLogRepository.save(jvmMemoryLog);
        }
    }

    /**
     * 监控普通对象
     */
    private void tourObjects() {
        try {
            log.info("JMT Begin calculate common Objects memory size!");
            List<CommonObjectMemoryDump> commonObjectMemoryDumpList = new ArrayList<>();
            for (String key : JMTWarden.getCollectionKeeper().keySet()) {
                Collection<Object> collection = JMTWarden.getCollectionKeeper().get(key);
                commonObjectMemoryDumpList.add(tourCollection(key, collection));
            }
            for (String key : JMTWarden.getMapKeeper().keySet()) {
                Map<Object, Object> map = JMTWarden.getMapKeeper().get(key);
                commonObjectMemoryDumpList.add(tourMap(key, map));
            }

            for (String key : JMTWarden.getObjectKeeper().keySet()) {
                Object object = JMTWarden.getObjectKeeper().get(key);
                commonObjectMemoryDumpList.add(tourObject(key, object));
            }

            if (!commonObjectMemoryDumpList.isEmpty() && jmtAutoConfiguration.isSaveData()) {
                objectMemoryDumpRepository.saveAll(commonObjectMemoryDumpList);
            }
            commonObjectMemoryDumpList.clear();
            log.info("JMT calculate common Objects  memory size end!");
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("JMT calculate common Objects  memory size failure!");
        }
    }

    /**
     * 获取某个对象的内存指标
     *
     * @param object     对象
     * @param objectName 对象名称
     * @return ThreadPoolMemoryDump
     */
    private CommonObjectMemoryDump tourObject(String objectName, Object object) {
        long size = JMTMemoryEstimate.jmtSizeOfObject(object);
        JMTWarden.objectMemorySizeMap.put(objectName, size);
        return new CommonObjectMemoryDump(0, size, objectName, object.getClass().toString());
    }


    /**
     * 获取某个集合的内存指标
     *
     * @param collection 对象
     * @param objectName 对象名称
     * @return ThreadPoolMemoryDump
     */
    private CommonObjectMemoryDump tourCollection(String objectName, Collection<Object> collection) {
        long size = JMTMemoryEstimate.estimateCollection(collection);
        JMTWarden.objectMemorySizeMap.put(objectName, size);
        return new CommonObjectMemoryDump(collection.size(), size, objectName, collection.getClass().toString());
    }

    /**
     * 获取某个Map的内存指标
     *
     * @param map        对象
     * @param objectName 对象名称
     * @return ThreadPoolMemoryDump
     */
    private CommonObjectMemoryDump tourMap(String objectName, Map<Object, Object> map) {
        long size = JMTMemoryEstimate.estimateMap(map);
        JMTWarden.objectMemorySizeMap.put(objectName, size);
        return new CommonObjectMemoryDump(map.size(), size, objectName, map.getClass().toString());
    }

    /**
     * 监控线程池
     */
    private void tourThreadPools() {
        try {
            log.info("JMT Begin calculate ThreadPools memory size!");
            List<ThreadPoolMemoryDump> threadPoolMemoryDumpList = new ArrayList<>();
            for (String key : JMTWarden.getThreadPoolKeeper().keySet()) {
                ThreadPoolExecutor threadPoolExecutor = JMTWarden.getThreadPoolKeeper().get(key);
                threadPoolMemoryDumpList.add(tourThreadPool(key, threadPoolExecutor));
            }
            if (!threadPoolMemoryDumpList.isEmpty() && jmtAutoConfiguration.isSaveData()) {
                threadPoolMemoryDumpRepository.saveAll(threadPoolMemoryDumpList);
            }
            threadPoolMemoryDumpList.clear();
            log.info("JMT calculate ThreadPools memory size end!");
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("JMT calculate ThreadPools memory size failure!");
        }
    }

    /**
     * 获取某个线程池的内存指标
     *
     * @param threadPoolName     线程池名称
     * @param threadPoolExecutor 线程池
     * @return ThreadPoolMemoryDump
     */
    private ThreadPoolMemoryDump tourThreadPool(String threadPoolName, ThreadPoolExecutor threadPoolExecutor) {
        ThreadPoolMemoryDump threadPoolMemoryDump = new ThreadPoolMemoryDump();
        threadPoolMemoryDump.setCreateDate(new Date());
        threadPoolMemoryDump.setStatus(ThreadPoolStatusEnum.getThreadPoolStatusEnum(threadPoolExecutor));
        threadPoolMemoryDump.setThreadPoolName(threadPoolName);
        if (!threadPoolExecutor.isTerminated()) {
            threadPoolMemoryDump.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
            threadPoolMemoryDump.setMaxPoolSize(threadPoolExecutor.getMaximumPoolSize());
            threadPoolMemoryDump.setQueueSize(threadPoolExecutor.getQueue().size());
            threadPoolMemoryDump.setQueueMemorySize(JMTMemoryEstimate.estimateCollection(threadPoolExecutor.getQueue()));
        }
        JMTWarden.threadPoolMemorySizeMap.put(threadPoolName, threadPoolMemoryDump);
        return threadPoolMemoryDump;
    }

    /**
     * 实时获取某个线程池当前的指标信息
     *
     * @param threadPoolName 线程池名称
     * @return ThreadPoolMemoryDump
     */
    private ThreadPoolMemoryDump getThreadPoolSnapshot(String threadPoolName) {
        if (JMTWarden.getThreadPoolKeeper().containsKey(threadPoolName)) {
            return tourThreadPool(threadPoolName, JMTWarden.getThreadPoolKeeper().get(threadPoolName));
        }
        return new ThreadPoolMemoryDump(ThreadPoolStatusEnum.NOT_EXIST);
    }
}
