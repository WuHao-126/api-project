package com.wuhao.project.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.model.response.LoginUserResponse;
import com.wuhao.project.model.entity.User;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/user")
@Api(value = "用户信息操作接口", tags = "用户信息操作接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;


    private static String SALT="wuhao";

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
       String email = userRegisterRequest.getEmail();
       //判断密码参数是否为空
       if(StringUtils.isAllEmpty(userPassword,checkPassword)){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
        //判断账号或者邮箱是否为空
       if(StringUtils.isAllEmpty(email,userAccount)){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
        long result = userService.userRegister(userRegisterRequest);
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserResponse loginUserResponse = userService.userLogin(userAccount, userPassword, servletRequest);
        if(loginUserResponse ==null){
            return Result.error(603,"账号或密码错误");
        }
        return Result.success(loginUserResponse.getId().toString());
    }

    /**
     * 申请AKSK
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/apply/ask")
    public Result applyAKSK(HttpServletRequest httpServletRequest){
        User loginUser = userService.getLoginUser(httpServletRequest);
        Long id = loginUser.getId();
        User user = userService.getById(id);
        if(user==null){
            throw new RuntimeException("非法申请AKSK,账户："+loginUser.getUserAccount());
        }
        //用户邮箱
        String email = user.getEmail();
        if(StringUtils.isEmpty(email)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String secretKey = user.getSecretKey();
        String accessKey = user.getAccessKey();
        String userAccount = user.getUserAccount();
        if(StringUtils.isAllEmpty(secretKey,accessKey)){
            //分配aksk
            String newAccessKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(5));
            String newSecretKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(8));
            user.setAccessKey(newAccessKey);
            user.setSecretKey(newSecretKey);
            userService.updateById(user);
            String emailContent="<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Document</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"email-box\">\n" +
                    "       <h1>API接口开放平台</h1>\n" +
                    "       <hr>\n" +
                    "       <div class=\"a\">\n" +
                    "            <p>尊敬的客户您好！</p>\n" +
                    "        <div>\n" +
                    "            <p>AccessKey:"+newAccessKey+"</p>\n" +
                    "            <p>SecretKey:"+newSecretKey+"</p>\n" +
                    "        </div>\n" +
                    "        <div>\n" +
                    "            <p>请谨慎保管，切勿泄露他人</p>\n" +
                    "       </div>\n" +
                    "       \n" +
                    "       </div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "<style>\n" +
                    "    *{\n" +
                    "        margin: 0;\n" +
                    "        padding: 0;\n" +
                    "    }\n" +
                    "    .a{\n" +
                    "        margin-top: 20px;\n" +
                    "    }\n" +
                    "    .email-box{\n" +
                    "        width: 600px;\n" +
                    "        height: 330px;\n" +
                    "        margin: 100px auto;\n" +
                    "        color: #666666;\n" +
                    "        text-align: center;\n" +
                    "    }\n" +
                    "</style>\n" +
                    "</html>";
            SimpleMailMessage smm = new SimpleMailMessage();
            //TODO
            smm.setFrom("1345498749@qq.com");//发送者
            smm.setTo(email);//收件人
            smm.setSubject("公钥密钥申请");//邮件主题
            smm.setText(emailContent);//邮件内容
            javaMailSender.send(smm);//发送邮件
            return Result.success();
        }else{
           throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
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
        User currentUser=userService.getLoginUser(servletRequest);
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
    @PostMapping("/update")
    public Result updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                              HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long id = loginUser.getId();
        Long id1 = userUpdateMyRequest.getId();
        String userRole = loginUser.getUserRole();
        //查看是否是本人操作 或者是超级管理操作
        if(id.equals(id1) || userRole.equals("superadmin")){
            User userAccount = userService.getOne(new QueryWrapper<User>().eq("userAccount", userUpdateMyRequest.getUserAccount()));
            BeanUtils.copyProperties(userUpdateMyRequest, userAccount);
            boolean result = userService.updateById(userAccount);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
            return Result.success(true);
        }
        //TODO 数据异常
        return null;
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/list/page")
//    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN_ROLE,UserConstant.ADMIN_ROLE})
    public Result listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Date beginDate = userQueryRequest.getBeginDate();
        Date endDate = userQueryRequest.getEndDate();
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(!StringUtils.isBlank(userQueryRequest.getUserRole()),"userRole",userQueryRequest.getUserRole())
                .like(!StringUtils.isBlank(userQueryRequest.getKeywords()),"userName",userQueryRequest.getKeywords())
                .or()
                .like(!StringUtils.isBlank(userQueryRequest.getKeywords()),"userAccount",userQueryRequest.getKeywords())
                .or()
                .like(!StringUtils.isEmpty(userQueryRequest.getKeywords()),"email",userQueryRequest.getKeywords())
                .or()
                .like(!StringUtils.isEmpty(userQueryRequest.getKeywords()),"phone",userQueryRequest.getKeywords())
                .between(beginDate!=null && endDate!=null,"createTime",beginDate,endDate)
                .eq(userQueryRequest.getUserState()!=null,"state",userQueryRequest.getUserState());

        Page<User> userPage = userService.page(new Page<>(current, size),queryWrapper);
        userPage.getRecords().stream().forEach(data ->{
            if(StringUtils.isEmpty(data.getAccessKey())){
                data.setIsExistKey(false);
            }else{
                data.setIsExistKey(true);
            }
        });
        return Result.success(userPage);
    }

    /**
     * 删除用户
     * @param deleteRequest
     * @param servletRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.SUPER_ADMIN_ROLE)
    public Result deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest servletRequest){
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return Result.success(b);
    }
}
