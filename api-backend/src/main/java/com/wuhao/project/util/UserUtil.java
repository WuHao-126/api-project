package com.wuhao.project.util;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.response.LoginUser;
import com.wuhao.project.model.response.LoginUserResponse;
import com.wuhao.project.security.UserContextHolder;
import com.wuhao.project.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */

public class UserUtil {


    public static LoginUserResponse getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserResponse loginUserResponse = new LoginUserResponse();
        BeanUtils.copyProperties(user, loginUserResponse);
        return loginUserResponse;
    }

    public static String getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return  authentication.getPrincipal().toString(); // 获取自定义用户信息
        }
        return null;
    }

}
