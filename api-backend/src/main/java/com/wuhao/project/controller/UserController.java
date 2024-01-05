package com.wuhao.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.common.Vo.LoginUserVO;
import com.wuhao.common.entity.User;
import com.wuhao.project.annotation.AuthCheck;
import com.wuhao.project.common.DeleteRequest;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import com.wuhao.project.constant.UserConstant;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.exception.ThrowUtils;
import com.wuhao.project.model.request.user.UserLoginRequest;
import com.wuhao.project.model.request.user.UserRegisterRequest;
import com.wuhao.project.model.request.user.UserQueryRequest;
import com.wuhao.project.model.request.user.UserUpdateMyRequest;
import com.wuhao.project.service.UserService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.wuhao.project.constant.UserConstant.USER_LOGIN_STATE;

@Controller
@RequestMapping("/user")
@ResponseBody
@Api(value = "用户信息操作接口", tags = "用户信息操作接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
       if(userRegisterRequest==null){
           throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
       }
       String userAccount=userRegisterRequest.getUserAccount();
       String userPassword=userRegisterRequest.getCheckPassword();
       String checkPassword=userRegisterRequest.getCheckPassword();
       if(StringUtils.isAllEmpty(userAccount,userPassword,checkPassword)){
           return null;
       }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return Result.success(result);
    }

    /**
     * 用户登录
     * @param
     * @param
     * @return
     */
    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest servletRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAllEmpty(userAccount,userPassword)){
            return null;
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, servletRequest);
        if(loginUserVO==null){
            return Result.error(603,"账号或密码错误");
        }
        return Result.success(loginUserVO.getId().toString());
    }

    /**
     * 获取当前用户
     * @param servletRequest
     * @return
     */
    @GetMapping("/current/{userId}")
    public Result getCurrentUser(@PathVariable("userId") String userId,HttpServletRequest servletRequest){
        if(servletRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO currentUser=userService.getLoginUser(servletRequest);
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long id = currentUser.getId();
        if(Long.parseLong(userId)==id){
            return Result.success(currentUser);
        }else{
            userService.userLogout(servletRequest);
            throw new BusinessException(ErrorCode.USER_STATUS_ERROR);
        }
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return Result.success(user);
    }

    @PostMapping("/accesskey")
    public User getaccessKeyUser( String accessKey){
        if(StringUtils.isBlank(accessKey)){
            throw new RuntimeException("accessKey为空");
        }
        return userService.getAccessKeyUser(accessKey);
    }
    /**
     * 当前用户退出
     * @param servletRequest
     * @return
     */
    @PostMapping("/logout")
    public Result userLogout(HttpServletRequest servletRequest){
        if(servletRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean flag=userService.userLogout(servletRequest);
        return Result.success(flag);
    }

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest 用户需要更改的数据
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public Result updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                              HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return Result.success(true);
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                   HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return Result.success(userPage);
    }

    /**
     * 删除用户
     * @param deleteRequest
     * @param servletRequest
     * @return
     */
    @PostMapping("/delete")
    public Result deleteUser(DeleteRequest deleteRequest, HttpServletRequest servletRequest){
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return Result.success(b);
    }
}
