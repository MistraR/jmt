package com.mistra.jmt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/2 23:00
 * @ Description: 通用对象内存占用数据实体类
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
    private int elementNumber;

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

    public CommonObjectMemoryDump() {

    }

    public int getElementNumber() {
        return elementNumber;
    }

    public void setElementNumber(int elementNumber) {
        this.elementNumber = elementNumber;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public CommonObjectMemoryDump(int elementNumber, long memorySize, String objectName, String objectClass) {
        this.setCreateDate(new Date());
        this.elementNumber = elementNumber;
        this.memorySize = memorySize;
        this.objectName = objectName;
        this.objectClass = objectClass;
    }

}
