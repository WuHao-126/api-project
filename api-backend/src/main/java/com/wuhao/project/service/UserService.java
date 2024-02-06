package com.wuhao.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.project.model.request.user.UserRegisterRequest;
import com.wuhao.project.model.vo.LoginUserVO;
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
     * @param servletRequest
     * @return
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest servletRequest);


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 退出登录
     * @param servletRequest
     * @return
     */
    boolean userLogout(HttpServletRequest servletRequest);


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

    Boolean isAdmin(HttpServletRequest servletRequest);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    User getAccessKeyUser(String accessKsy);

}
