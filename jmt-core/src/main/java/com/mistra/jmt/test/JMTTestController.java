package com.mistra.jmt.test;


import cn.hutool.core.thread.NamedThreadFactory;
import com.mistra.jmt.model.ThreadPoolMemoryDump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/4 23:20
 * @ Description: 用来测试的，模拟实际项目中的使用
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@RestController
@RequestMapping("/jmt/test")
public class JMTTestController {

    @Autowired
    private JobQueueTestLauncher jobQueueTestLauncher;

    @GetMapping("/getThreadPoolExecutorInfo")
    public ThreadPoolMemoryDump getThreadPoolExecutorInfo(String name){

    }
}
