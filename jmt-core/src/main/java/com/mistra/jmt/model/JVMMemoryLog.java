package com.mistra.jmt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/4 23:20
 * @ Description: JVM内存占用量记录
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Entity
@Table(name = "jvm_memory_log")
public class JVMMemoryLog extends JMTBaseEntity implements Serializable {

    /**
     * 剩余内存
     */
    @Column(name = "free_memory")
    private long freeMemory;

    /**
     * 使用内存
     */
    @Column(name = "used__memory")
    private long usedMemory;

    /**
     * 总内存大小
     */
    @Column(name = "total__memory")
    private long totalMemory;

    /**
     * 使用量
     */
    @Column(name = "used_ratio")
    private long usedRatio;

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public long getUsedRatio() {
        return usedRatio;
    }

    public void setUsedRatio(long usedRatio) {
        this.usedRatio = usedRatio;
    }
}
