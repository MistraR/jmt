package com.mistra.jmt.model;

import lombok.Data;

import javax.persistence.Table;
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
@Data
@Table(name = "thread_pool_memory_dump")
public class ThreadPoolMemoryDump extends BaseEntity implements Serializable {
}
