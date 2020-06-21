package com.mistra.jmt.model;

import com.mistra.jmt.core.ThreadPoolStatusEnum;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/2 22:58
 * @ Description: 线程池内存快照实体类
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Builder
@Entity
@Table(name = "thread_pool_memory_dump")
public class ThreadPoolMemoryDump extends JMTBaseEntity implements Serializable {

    /**
     * 当前核心线程数
     */
    @Column(name = "core_pool_size")
    private int corePoolSize;

    /**
     * 当前最大线程数
     */
    @Column(name = "max_pool_size")
    private int maxPoolSize;

    /**
     * 线程池状态
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private ThreadPoolStatusEnum status;

    /**
     * 队列任务数
     */
    @Column(name = "queue_size")
    private int queueSize;

    /**
     * 队列占用内存大小
     */
    @Column(name = "queue_memory_size")
    private long queueMemorySize;

    /**
     * 线程池名称
     */
    @Column(name = "thread_pool_name")
    private String threadPoolName;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public ThreadPoolStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ThreadPoolStatusEnum status) {
        this.status = status;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public long getQueueMemorySize() {
        return queueMemorySize;
    }

    public void setQueueMemorySize(long queueMemorySize) {
        this.queueMemorySize = queueMemorySize;
    }

    public String getThreadPoolName() {
        return threadPoolName;
    }

    public void setThreadPoolName(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }
}
