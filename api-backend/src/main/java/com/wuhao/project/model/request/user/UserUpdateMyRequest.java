package com.wuhao.project.model.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新个人信息请求
 */
@Data
public class UserUpdateMyRequest implements Serializable {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 用户旧密码
     */
    private String oldUserPassword;
    /**
     * 用户签名
     */
    private String description;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 简介
     */
    private String userProfile;
    /**
     * 权限
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}