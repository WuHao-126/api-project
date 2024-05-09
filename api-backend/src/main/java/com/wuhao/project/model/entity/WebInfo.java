package com.wuhao.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
@TableName("tb_web_info")
public class WebInfo {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 网站名称
     */
    private String name;
    /**
     * 网站logo
     */
    private String logo;
    /**
     * 网站描述
     */
    private String description;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 公告
     */
    @TableField(exist = false)
    private String notice;

    /**
     * 时间,天数
     */
    @TableField(exist = false)
    private Integer day;


}