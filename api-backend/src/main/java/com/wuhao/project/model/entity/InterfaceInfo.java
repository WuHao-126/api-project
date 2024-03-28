package com.wuhao.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息
 * @TableName interface_info
 */
@Data
@TableName(value ="tb_interface_info")
public class InterfaceInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 接口类型
     */
    private Integer type;
    /**
     * 接口封面
     */
    private String cover;
    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     * [
     *   {"name": "username", "type": "string"}
     * ]
     */
    private String requestFieldParams;

    /**
     * 请求头
     */
    private String requestHeaderParams;

    /**
     * 返回类型
     */
    private String responseType;

    /**
     * 相应的参数
     */
    private String responseFieldParams;
    /**
     * 响应参数示例
     */
    private String responseParamsExample;
    /**
     * 错误参照码
     */
    private String errorCode;
    /**
     * 代码示例
     */
    private String codeExample;
    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer state;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人
     */
    private Long createBy;
    /**
     *
     */
    @TableField(exist = false)
    private String createByName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer isDelete;
    /**
     * 接口被调用次数
     */
    private Integer useTotal;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}