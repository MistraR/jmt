package com.mistra.jmt.core;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/1 22:18
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@ComponentScan(basePackages = {"com.mistra.jmt"})
@EntityScan("com.mistra.jmt.model")
@EnableScheduling
@Configuration
@ConfigurationProperties(prefix = "jmt")
public class JMTAutoConfiguration {

    /**
     * 估计计算的样本大小
     */
    private int capacity = 100;

    /**
     * 监控数据是否需要入库
     */
    private boolean saveData = true;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isSaveData() {
        return saveData;
    }

    public void setSaveData(boolean saveData) {
        this.saveData = saveData;
    }
}
