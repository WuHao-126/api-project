package com.wuhao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.common.Vo.LoginUserVO;
import com.wuhao.common.entity.User;


import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface UserService extends IService<User> {



    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @return
     */
    LoginUserVO userLogin(String userAccount, String userPassword);


}
