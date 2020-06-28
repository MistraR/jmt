package com.mistra.jmt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/2 23:00
 * @ Description: 任务队列内存快照
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Entity
@Table(name = "object_memory_dump")
public class CommonObjectMemoryDump extends JMTBaseEntity implements Serializable {

    /**
     * 若对象为Collect或者Map，则设置元素个数
     */
    @Column(name = "element_number")
    private int element_number;

    /**
     * 对象占用内存大小
     */
    @Column(name = "memory_size")
    private long memorySize;

    /**
     * 对象名称
     */
    @Column(name = "object_name")
    private String objectName;

    /**
     * 对象类型
     */
    @Column(name = "object_class")
    private String objectClass;
}
