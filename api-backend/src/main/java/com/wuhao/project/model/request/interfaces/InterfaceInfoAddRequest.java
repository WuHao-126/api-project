package com.wuhao.project.model.request.interfaces;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;
    /**
     * 接口类型
     */
    private Integer type;
    /**
     * 请求头参数
     */
    private String requestHeaderParams;
    /**
     * 请求参数
     */
    private String requestFieldParams;
    /**
     * 响应类型
     */
    private String responseFieldParams;
    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 请求方法类型
     */
    private String method;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
}