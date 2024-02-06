package com.wuhao.project.model.request.interfaces;

import lombok.Data;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: 接口参数
 */
@Data
public class RequestFieldParam {
    private Integer id;
    private String filedName;
    private String type;
    private String desc;
    private String required;
}
