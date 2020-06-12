package com.mistra.jmt.core;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/11 22:02
 * @ Description: 线程池状态枚举
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
public enum ThreadPoolStatusEnum {

    //初始化状态,能够接收新任务，以及对已添加的任务进行处理
    RUNNING(1, "运行中"),
    //不接收新任务，但能处理已添加的任务
    SHUTDOWN(2, "关闭状态"),
    //不接收新任务，不处理已添加的任务，并且会中断正在处理的任务
    STOP(3, "已停止"),
    //当所有的任务已终止，workerCount (有效线程数)为0 ，线程池会变为TIDYING状态
    TIDYING(4, "TIDYING"),
    //线程池彻底终止，就变成TERMINATED状态
    TERMINATED(5, "彻底终止"),
    NOT_EXIST(6, "不存在的线程池");

    private int value;

    private String describe;

    ThreadPoolStatusEnum(int value, String describe) {
        this.value = value;
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }

    public int getValue() {
        return value;
    }

    public static ThreadPoolStatusEnum getByValue(int value) {
        for (ThreadPoolStatusEnum statusEnum : ThreadPoolStatusEnum.values()) {
            if (Objects.equals(value, statusEnum.getValue())) {
                return statusEnum;
            }
        }
        throw new IllegalArgumentException();
    }

    public static ThreadPoolStatusEnum getThreadPoolStatusEnum(ThreadPoolExecutor threadPoolExecutor) {
        if (threadPoolExecutor.isShutdown()) {
            return SHUTDOWN;
        } else if (threadPoolExecutor.isTerminating()) {
            return TIDYING;
        } else if (threadPoolExecutor.isTerminated()) {
            return TERMINATED;
        }
        if (threadPoolExecutor.getTaskCount() == 0 && threadPoolExecutor.getActiveCount() == 0) {
            return STOP;
        } else {
            return RUNNING;
        }
    }

}
