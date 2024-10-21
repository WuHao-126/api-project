package com.wuhao.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.project.model.request.user.UserRegisterRequest;
import com.wuhao.project.model.response.FirstTokenResponse;
import com.wuhao.project.model.response.LoginUserResponse;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.request.user.UserQueryRequest;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface UserService extends IService<User> {

    /**
     * 注册用户
     * @return
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @return
     */
    FirstTokenResponse userLogin(String userAccount, String userPassword, String email);


    /**
     * 获取当前登录用户
     *
     * @return
     */
    User getLoginUser();


    /**
     * 退出登录
     * @return
     */
    boolean userLogout();


    /**
     * 修改用户信息（自己本人修改，或者管理员修改）
     * @param user
     * @param loginUser
     * @return
     */
    Boolean updateUser(User user, User loginUser);
    /**
     * 是否为管理员
     * @param user
     * @return
     */
    Boolean isAdmin(User user);


    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    User getAccessKeyUser(String accessKsy);

}
