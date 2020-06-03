package com.mistra.jmt.model;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/2 22:59
 * @ Description: 系统缓存内存快照
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Data
@Table(name = "system_cache_memory_dump")
public class SystemCacheMemoryDump extends BaseEntity implements Serializable {
}
