package com.mistra.jmt.model;

import lombok.Data;

import javax.persistence.Id;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/2 23:24
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Data
public class BaseEntity {

    @Id
    private String id;


}
