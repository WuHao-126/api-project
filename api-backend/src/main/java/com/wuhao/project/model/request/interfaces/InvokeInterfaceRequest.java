package com.wuhao.project.model.request.interfaces;

import lombok.Data;

import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class InvokeInterfaceRequest {
    private Long id;
    private String url;
    private String method;
    private List<RequestHeaderParam> requestHeaderParams;
    private List<RequestFieldParam> requestFieldParams;
}
