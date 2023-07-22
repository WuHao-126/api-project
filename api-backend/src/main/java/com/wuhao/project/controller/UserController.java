package com.wuhao.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.common.Vo.LoginUserVO;
import com.wuhao.common.entity.User;
import com.wuhao.project.annotation.AuthCheck;
import com.wuhao.project.common.BaseResponse;
import com.wuhao.project.common.DeleteRequest;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.ResultUtils;
import com.wuhao.project.constant.UserConstant;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.exception.ThrowUtils;
import com.wuhao.project.model.request.UserLoginRequest;
import com.wuhao.project.model.request.UserRegisterRequest;
import com.wuhao.project.model.user.UserQueryRequest;
import com.wuhao.project.model.user.UserUpdateMyRequest;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
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
        return ResultUtils.success(result);
    }

    @GetMapping("/aaa")
    public String aaa(){
        return "aaa";
    }

    /**
     * 用户登录
     * @param
     * @param
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest servletRequest) {
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
            return ResultUtils.error(603,"账号或密码错误");
        }
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取当前用户
     * @param servletRequest
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<LoginUserVO> currentUser(HttpServletRequest servletRequest){
        if(servletRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO currentUser = (LoginUserVO) servletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long id = currentUser.getId();
        User byId = userService.getById(id);
        LoginUserVO LoginUserVO=new LoginUserVO();
        BeanUtils.copyProperties(currentUser,LoginUserVO);
        return ResultUtils.success(LoginUserVO);
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
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
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
    public BaseResponse<Boolean> userLogout(HttpServletRequest servletRequest){
        if(servletRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean flag=userService.userLogout(servletRequest);
        return ResultUtils.success(flag);
    }

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest 用户需要更改的数据
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
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
        return ResultUtils.success(true);
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
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                   HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 删除用户
     * @param deleteRequest
     * @param servletRequest
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(DeleteRequest deleteRequest, HttpServletRequest servletRequest){
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }
}
