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
 * @ Description: 计算各个监控对象的内存占用情况
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

    @Scheduled(fixedDelay = 300000)
    private void tour() {
        tourThreadPools();
        tourObjects();
        tourJVM();
    }

    /**
     * 监控JVM指标
     */
    private void tourJVM() {
        JVMMemoryLog jvmMemoryLog = new JVMMemoryLog();
        jvmMemoryLog.setCreateDate(new Date());
        RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();
        jvmMemoryLog.setUsedMemory(runtimeInfo.getUsableMemory());
        jvmMemoryLog.setFreeMemory(runtimeInfo.getFreeMemory());
        jvmMemoryLog.setTotalMemory(runtimeInfo.getMaxMemory());
        jvmMemoryLog.setUsedRatio(runtimeInfo.getUsableMemory() / runtimeInfo.getMaxMemory());
        jvmMemoryLogRepository.save(jvmMemoryLog);
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
                commonObjectMemoryDumpList.add(tourObject(key, collection, collection.size()));
            }
            for (String key : JMTWarden.getMapKeeper().keySet()) {
                Map<Object, Object> map = JMTWarden.getMapKeeper().get(key);
                commonObjectMemoryDumpList.add(tourObject(key, map, map.size()));
            }

            for (String key : JMTWarden.getObjectKeeper().keySet()) {
                Object object = JMTWarden.getObjectKeeper().get(key);
                commonObjectMemoryDumpList.add(tourObject(key, object, 0));
            }

            if (!commonObjectMemoryDumpList.isEmpty()) {
                objectMemoryDumpRepository.saveAll(commonObjectMemoryDumpList);
            }
            log.info("JMT calculate common Objects  memory size end!");
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("JMT calculate common Objects  memory size failure!");
        }
    }

    /**
     * 获取某个对象的内存指标
     *
     * @param object        对象
     * @param objectName    对象名称
     * @param elementNumber 若对象为Collect或者Map，则设置元素个数
     * @return ThreadPoolMemoryDump
     */
    private CommonObjectMemoryDump tourObject(String objectName, Object object, int elementNumber) {
        CommonObjectMemoryDump commonObjectMemoryDump = new CommonObjectMemoryDump();
        commonObjectMemoryDump.setCreateDate(new Date());
        commonObjectMemoryDump.setObjectName(objectName);
        commonObjectMemoryDump.setObjectClass(object.getClass().toString());
        commonObjectMemoryDump.setElementNumber(elementNumber);
        commonObjectMemoryDump.setMemorySize(JMTMemoryEstimate.shallowSizeOf(object));
        return commonObjectMemoryDump;
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
            if (!threadPoolMemoryDumpList.isEmpty()) {
                threadPoolMemoryDumpRepository.saveAll(threadPoolMemoryDumpList);
            }
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
            threadPoolMemoryDump.setQueueMemorySize(JMTMemoryEstimate.shallowSizeOf(threadPoolExecutor.getQueue()));
        }
        return threadPoolMemoryDump;
    }

    /**
     * 获取某个线程池当前的指标信息
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
