package com.mistra.jmt.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Mistra
 * @ Version: 1.0
 * @ Time: 2020/6/2 23:00
 * @ Description: 任务队列内存快照
 * @ Copyright (c) Mistra,All Rights Reserved.
 * @ Github: https://github.com/MistraR
 * @ CSDN: https://blog.csdn.net/axela30w
 */
@Entity
@Table(name = "job_queue_memory_dump")
public class JobQueueMemoryDump extends JMTBaseEntity implements Serializable {
}
