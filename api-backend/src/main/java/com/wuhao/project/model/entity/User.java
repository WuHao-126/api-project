package com.wuhao.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @TableName user
 */
@TableName(value = "tb_user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户昵称
     */
    private String description;
    /**
     * 用户手机号
     */
    private Long phone;
    /**
     * 用户邮箱
     */
     private String email;
    /**
     * 用户角色: user, admin ,superadmin
     */
    private String userRole;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 签名 accessKey
     */
    private String accessKey;

    /**
     * 签名 secretKey
     */
    private String secretKey;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 用户状态(0-正常 1-封号 2-注销)
     */
    private Integer state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Boolean isExistKey;
}