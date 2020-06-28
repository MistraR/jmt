package com.mistra.jmt.core;

import com.mistra.jmt.dao.ThreadPoolMemoryDumpRepository;
import com.mistra.jmt.model.ThreadPoolMemoryDump;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
@Slf4j
@Component
public class TourMemory {

    @Autowired
    private ThreadPoolMemoryDumpRepository threadPoolMemoryDumpRepository;

    @Scheduled(fixedDelay = 300000)
    private void tour() {
        tourThreadPools();
    }

    /**
     * 监控线程池
     */
    private void tourThreadPools() {
        try {
            log.info("");
            List<ThreadPoolMemoryDump> threadPoolMemoryDumpList = new ArrayList<>();
            for (String key : JMTWarden.getThreadPoolKeeper().keySet()) {
                ThreadPoolExecutor threadPoolExecutor = JMTWarden.getThreadPoolKeeper().get(key);
                if (!threadPoolExecutor.isTerminated()) {
                    threadPoolMemoryDumpList.add(tourThreadPool(threadPoolExecutor));
                } else {
                    threadPoolMemoryDumpList.add(ThreadPoolMemoryDump.builder()
                            .status(ThreadPoolStatusEnum.getThreadPoolStatusEnum(threadPoolExecutor))
                            .build());
                    log.info("The thread pool is stopped!");
                }
            }
            if (!threadPoolMemoryDumpList.isEmpty()) {
                threadPoolMemoryDumpRepository.saveAll(threadPoolMemoryDumpList);
            }
        } catch (IllegalAccessException illegalAccessException) {
            log.error("");
        } catch (Exception exception) {
            log.error("");
        }
    }

    /**
     * 统计线程池快照指标
     *
     * @param threadPoolExecutor 线程池
     * @return 统计信息
     */
    private ThreadPoolMemoryDump tourThreadPool(ThreadPoolExecutor threadPoolExecutor) throws IllegalAccessException {
        return ThreadPoolMemoryDump.builder()
                .corePoolSize(threadPoolExecutor.getCorePoolSize())
                .maxPoolSize(threadPoolExecutor.getMaximumPoolSize())
                .queueSize(threadPoolExecutor.getQueue().size())
                .queueMemorySize(SizeOfObjectUtil.fullSizeOf(threadPoolExecutor.getQueue()))
                .build();
    }

    /**
     * 获取某个线程池当前的指标信息
     *
     * @param threadPoolName 线程池名称
     * @return ThreadPoolMemoryDump
     * @throws IllegalAccessException
     */
    private ThreadPoolMemoryDump getThreadPoolSnapshot(String threadPoolName) throws IllegalAccessException {
        if (JMTWarden.getThreadPoolKeeper().containsKey(threadPoolName)) {
            ThreadPoolExecutor threadPoolExecutor = JMTWarden.getThreadPoolKeeper().get(threadPoolName);
            if (!threadPoolExecutor.isTerminated()) {
                return tourThreadPool(threadPoolExecutor);
            } else {
                return ThreadPoolMemoryDump.builder().status(ThreadPoolStatusEnum.getThreadPoolStatusEnum(threadPoolExecutor)).build();
            }
        }
        return ThreadPoolMemoryDump.builder().status(ThreadPoolStatusEnum.NOT_EXIST).build();
    }
}
