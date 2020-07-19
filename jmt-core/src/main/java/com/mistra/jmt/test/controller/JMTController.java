package com.mistra.jmt.test.controller;

import com.mistra.jmt.test.service.JMTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/1 22:20
 * @ Description: 用于测试，往目标对象添加元素
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@RestController
@RequestMapping("/mistra/jmt")
public class JMTController {

    private static final Logger log = LoggerFactory.getLogger(JMTController.class);

    private static final String SUCCESS = "success";

    @Autowired
    private JMTService jmtService;

    /**
     * 测试：添加线程池待处理任务数
     *
     * @param number 数量
     * @return SUCCESS
     */
    @GetMapping(value = "/addThreadTask/{number}")
    public String addThreadTask(@PathVariable("number") int number) {
        jmtService.addThreadTask(number);
        return SUCCESS;
    }

    /**
     * 测试：添加集合元素
     *
     * @param number 数量
     * @return SUCCESS
     */
    @GetMapping(value = "/addCollectionElement/{number}")
    public String addCollectionElement(@PathVariable("number") int number) {
        jmtService.addCollectionElement(number);
        return SUCCESS;
    }

    /**
     * 测试：添加Map元素
     *
     * @param number 数量
     * @return SUCCESS
     */
    @GetMapping(value = "/addMapElement/{number}")
    public String addMapElement(@PathVariable("number") int number) {
        jmtService.addMapElement(number);
        return SUCCESS;
    }

    /**
     * 测试：添加Object
     *
     * @param number 数量
     * @return SUCCESS
     */
    @GetMapping(value = "/addObject/{number}")
    public String addObject(@PathVariable("number") int number) {
        jmtService.addObject(number);
        return SUCCESS;
    }
}
