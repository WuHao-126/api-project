package com.wuhao.project.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.common.IdRequest;
import com.wuhao.project.constant.CommonConstant;
import com.wuhao.project.model.entity.Blog;
import com.wuhao.project.model.request.user.*;
import com.wuhao.project.model.response.FirstTokenResponse;
import com.wuhao.project.model.response.LoginUser;
import com.wuhao.project.model.response.LoginUserResponse;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.annotation.AuthCheck;
import com.wuhao.project.common.DeleteRequest;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import com.wuhao.project.constant.UserConstant;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.exception.ThrowUtils;
import com.wuhao.project.service.BlogService;
import com.wuhao.project.service.CommentService;
import com.wuhao.project.service.UserService;
import com.wuhao.project.util.RegexUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.wuhao.project.constant.UserConstant.USER_LOGIN_STATE;

@Slf4j
@RestController
@RequestMapping("/user")
@Api(value = "用户信息操作接口", tags = "用户信息操作接口")
public class UserController {

    @Autowired
    private UserService userService;


    private static String SALT="wuhao";

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        userService.userRegister(userRegisterRequest);
        return Result.success();
    }

    /**
     * 用户登录
     * @param
     * @param
     * @return
     */
    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        return Result.success(userService.userLogin(userLoginRequest));
    }

    /**
     * 当前用户退出
     * @return
     */
    @PostMapping("/logout")
    public Result userLogout(){
        return Result.success(userService.userLogout());
    }

    /**
     * 管理员账号登录
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/admin/login")
    public Result adminLogin(@RequestBody UserLoginRequest userLoginRequest){
        return Result.success(userService.adminLogin(userLoginRequest));
    }

    /**
     * 申请AKSK,发送到邮箱
     * @return
     */
    @PostMapping("/apply/ask")
    public Result applyAsk(){
        userService.applyAsk();
        return Result.success();
    }

    /**
     * 获取当前登录用户
     * @return
     */
    @GetMapping("/current")
    public Result getCurrentUser(){
        return Result.success(userService.getLoginUser());
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public Result getUserById(long id) {
        User user = userService.getById(id);
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return Result.success(user);
    }


    @PostMapping("/accesskey")
    public User getAccessKeyUser( String accessKey){
        if(StringUtils.isBlank(accessKey)){
            throw new RuntimeException("accessKey为空");
        }
        return userService.getAccessKeyUser(accessKey);
    }

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest 用户需要更改的数据
     * @return
     */
    @PostMapping("/update")
    public Result updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest) {
        return Result.success(userService.updateMyUser(userUpdateMyRequest));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/list/page")
//    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN_ROLE,UserConstant.ADMIN_ROLE})
    public Result getUserListPage(@RequestBody UserQueryRequest userQueryRequest) {
        return Result.success( userService.getUserListPage(userQueryRequest));
    }

    /**
     * 删除用户
     * @param idRequest
     * @return
     */
    @DeleteMapping("/delete")
//    @AuthCheck(mustRole = UserConstant.SUPER_ADMIN_ROLE)
    public Result deleteUser(@RequestBody IdRequest idRequest){
        return Result.success(userService.deleteUser(idRequest));
    }

}
