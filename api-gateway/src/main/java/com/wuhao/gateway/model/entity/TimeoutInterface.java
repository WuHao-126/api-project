package com.wuhao.gateway.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
@TableName("tb_timeout_interface")
public class TimeoutInterface {
    private Integer id;
    private Long interfaceId;
    private Double responseTime;
    private LocalDateTime createTime;
}
