package com.mistra.jmt.dao;

import com.mistra.jmt.model.JVMMemoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/28 21:06
 * @ Description:
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Repository
public interface JVMMemoryLogRepository extends JpaRepository<JVMMemoryLog, Integer> {
}
