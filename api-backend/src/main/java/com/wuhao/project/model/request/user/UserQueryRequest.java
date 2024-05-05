package com.wuhao.project.model.request.user;


import com.wuhao.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户查询请求
 *
 */
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 关键字 昵称  账号  邮箱 手机号
     */
    private String keywords;
    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;
    /**
     * 用户状态
     */
    private String userState;
    /**
     * 时间范围
     */
    private Date beginDate;

    private Date endDate;

    private static final long serialVersionUID = 1L;
}