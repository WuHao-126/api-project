package com.wuhao.project.model.response;

import lombok.Data;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class LoginUser {
    /**
     * 用户token
     */
    private String token;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 用户名称
     */
    private String username;
}
