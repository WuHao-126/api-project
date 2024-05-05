package com.wuhao.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
@TableName("tb_interface_customized")
public class Customized {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 用户ID
     */
    private Long userId;

    @TableField(exist = false)
    private String userName;
    /**
     * 申请接口名称名称
     */
    private String name;
    /**
     * 业务需求描述
     */
    private String demandDescription;
    /**
     * 功能需求描述
     */
    private String functionDescription;
    /**
     * 技术要求和限制
     */
    private String technologyDescription;
    /**
     * 预算
     */
    private Double budget;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;
}
