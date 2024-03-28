package com.wuhao.project.model.request.user;

import lombok.Data;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class UserEmailCodeRequest {
    /**
     * 邮箱
     */
    private String email;
    /**
     * 验证码
     */
    private String code;
}
