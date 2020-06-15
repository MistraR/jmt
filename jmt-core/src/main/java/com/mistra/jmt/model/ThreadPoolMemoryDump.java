package com.mistra.jmt.model;

import com.mistra.jmt.core.ThreadPoolStatusEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/2 22:58
 * @ Description: 线程池内存快照
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@Table(name = "thread_pool_memory_dump")
public class ThreadPoolMemoryDump extends JMTBaseEntity implements Serializable {

    @Column(name = "core_pool_size")
    private int corePoolSize;

    @Column(name = "max_pool_size")
    private int maxPoolSize;

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
    private String queueMemorySize;

    @Column(name = "thread_pool_name")
    private String threadPoolName;

}
