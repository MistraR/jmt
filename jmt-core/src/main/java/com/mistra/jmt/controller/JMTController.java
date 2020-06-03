package com.mistra.jmt.controller;

import com.mistra.jmt.service.JMTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/1 22:20
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Slf4j
@RestController
@RequestMapping("/jmt")
public class JMTController {

    @Autowired
    private JMTService jmtService;
}
