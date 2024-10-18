package com.wuhao.project.model.response;

import lombok.Data;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class FirstTokenResponse {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * token
     */
    private String token;
}
