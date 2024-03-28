package com.wuhao.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
@TableName("tb_exceptional_log")
public class ExceptionalLog {
    /**
     * id
     */
    private Integer id;
    /**
     * 异常名称
     */
    private String name;
    /**
     * 异常状态码
     */
    private Integer code;
    /**
     * 异常描述
     */
    private String description;
    /**
     * 发生时间
     */
    private LocalDateTime createTime;
}
