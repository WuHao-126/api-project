package com.wuhao.project.model.entity;

import lombok.Data;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class SystemInfo {
    //cpu利用率
    private Integer cpuUsed;
    //内存占用率
    private Integer memoryUsed;
    //磁盘总量
    private Long diskTotal;
    //磁盘剩余量
    private Long diskResidue;
    //磁盘已用量
    private Long usedDiaskTotal;
}
