package com.mistra.jmt.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/4 23:20
 * @ Description: JVM内存占用量记录
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Data
@Builder
public class JVMMemoryLog {

    private long  freeMemory;
    private long  usedMemory;
    private long  totalMemory;

}
