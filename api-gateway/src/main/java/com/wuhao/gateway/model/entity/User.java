package com.wuhao.gateway.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
     * 用户手机号
     */
    private Long phone;
    /**
     * 用户邮箱
     */
     private String email;
    /**
     * 用户角色: user, admin , superadmin
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
     * 用户状态
     */
    @TableLogic
    private Integer state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}