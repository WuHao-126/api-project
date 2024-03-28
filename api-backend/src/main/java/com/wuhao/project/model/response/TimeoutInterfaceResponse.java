package com.wuhao.project.model.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class TimeoutInterfaceResponse {
    /**
     * id
     */
    private Integer id;
    /**
     * 接口id
     */
    private Long interfaceId;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 接口url
     */
    private String url;
    /**
     * 响应时间
     */
    private String responseTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
