package com.wuhao.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.model.entity.User;

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
    User userLogin(String userAccount, String userPassword);


}
