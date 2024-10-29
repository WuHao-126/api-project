package com.wuhao.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.project.common.IdRequest;
import com.wuhao.project.model.request.user.UserLoginRequest;
import com.wuhao.project.model.request.user.UserRegisterRequest;
import com.wuhao.project.model.request.user.UserUpdateMyRequest;
import com.wuhao.project.model.response.FirstTokenResponse;
import com.wuhao.project.model.response.LoginUser;
import com.wuhao.project.model.response.LoginUserResponse;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.request.user.UserQueryRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface UserService extends IService<User> {

    /**
     * 注册用户
     * @return
     */
    void userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param userLoginRequest
     * @return
     */
    FirstTokenResponse userLogin(UserLoginRequest userLoginRequest);

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

    User getAccessKeyUser(String accessKsy);

    Page<User> getUserListPage(UserQueryRequest userQueryRequest);

    void applyAsk();

    LoginUser adminLogin(UserLoginRequest userLoginRequest);

    Boolean updateMyUser(UserUpdateMyRequest userUpdateMyRequest);

    Boolean deleteUser(IdRequest idRequest);
}
