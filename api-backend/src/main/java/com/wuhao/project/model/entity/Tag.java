package com.wuhao.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@TableName("sys_tag")
@Data
public class Tag {
    private Integer id;
    private String name;
    private String type;
    private String other;
    private Date createTime;
}
