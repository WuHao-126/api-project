package com.wuhao.project.model.request.interfaces;

import com.wuhao.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 * @author yupi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {


    /**
     * 请求路径
     */
    private String url;
    /**
     * 关键词
     */
    private String keywords;
    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer state;

    /**
     * 请求类型
     */
    private String method;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 接口类型
     */
    private Integer type;

    /**
     * 时间范围
     */
    private Date beginDate;

    private Date endDate;

}