package com.wuhao.project.controller;

import cn.hutool.core.util.RandomUtil;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import com.wuhao.project.constant.CommonConstant;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.request.user.UserEmailCodeRequest;
import com.wuhao.project.service.UserService;
import com.wuhao.project.util.RegexUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/code")
    public Result sendEmailCode(@RequestBody UserEmailCodeRequest request, HttpServletRequest httpServletRequest){
        User loginUser = userService.getLoginUser(httpServletRequest);
        if(loginUser==null){
            return Result.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        String email1 = loginUser.getEmail();
        if(!StringUtils.isEmpty(email1)){
            return Result.error(ErrorCode.ALREADY_REGISTER);
        }
        String email = request.getEmail();
        if(!RegexUtils.isEmailInvalid(email)){
            return Result.error(ErrorCode.PARAMS_ERROR);
        };
        User email2 = userService.query().eq("email", email).one();
        if(email2!=null){
            return Result.error(ErrorCode.ALREADY_REGISTER);
        }
        SimpleMailMessage smm = new SimpleMailMessage();
        String s = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(CommonConstant.EMAIL_CODE+email,s,3, TimeUnit.MINUTES);
        String emailContent="您的验证码为："+s;
        smm.setFrom("1345498749@qq.com");//发送者
        smm.setTo(email);//收件人
        smm.setSubject("欢迎申请API接口");//邮件主题
        smm.setText(emailContent);//邮件内容
//        javaMailSender.send(smm);//发送邮件
        return Result.success();
    }

    @PostMapping("/send")
    public Result sendEmailCode(@RequestBody UserEmailCodeRequest request){
        String email = request.getEmail();
        if(!RegexUtils.isEmailInvalid(email)){
            return Result.error(ErrorCode.PARAMS_ERROR);
        };
        User user = userService.query().eq("email", email).one();
        if(user!=null){
            return Result.error(ErrorCode.ALREADY_REGISTER);
        }
        SimpleMailMessage smm = new SimpleMailMessage();
        String s = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(CommonConstant.EMAIL_CODE+email,s,3, TimeUnit.MINUTES);
        String emailContent="您的验证码为："+s;
        smm.setFrom("1345498749@qq.com");//发送者
        smm.setTo(email);//收件人
        smm.setSubject("欢迎申请API接口");//邮件主题
        smm.setText(emailContent);//邮件内容
//        javaMailSender.send(smm);//发送邮件
        return Result.success();
    }

    @PostMapping("/check/code")
    public Result checkCode(@RequestBody UserEmailCodeRequest userEmailCodeRequest){
        String email = userEmailCodeRequest.getEmail();
        String code = userEmailCodeRequest.getCode();
        if(StringUtils.isAnyBlank(email,code)){
            return Result.error(ErrorCode.PARAMS_ERROR);
        }
        String s = redisTemplate.opsForValue().get(CommonConstant.EMAIL_CODE + email);
        if(!code.equals(s)){
            return Result.error(ErrorCode.ERROR_CODE);
        }
        return Result.success();
    }

    @PostMapping("/bind")
    public Result bindEmail(@RequestBody UserEmailCodeRequest userEmailCodeRequest, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        if(loginUser==null){
            return Result.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        String email = userEmailCodeRequest.getEmail();
        String code = userEmailCodeRequest.getCode();
        if(StringUtils.isAnyBlank(email,code)){
            return Result.error(ErrorCode.PARAMS_ERROR);
        }
        String s = redisTemplate.opsForValue().get(CommonConstant.EMAIL_CODE + email);
        if(!code.equals(s)){
            return Result.error(ErrorCode.ERROR_CODE);
        }
        loginUser.setEmail(email);
        userService.updateById(loginUser);
        return Result.success();
    }
}
