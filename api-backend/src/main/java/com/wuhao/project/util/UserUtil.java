package com.wuhao.project.util;

import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.response.LoginUser;
import com.wuhao.project.model.response.LoginUserResponse;
import com.wuhao.project.security.SecurityContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;


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

    public static LoginUser getLoginUser() {
        return SecurityContextHolder.get("loginUser",LoginUser.class);
    }

    public static String getUserId(){
        LoginUser loginUser = getLoginUser();
        if(loginUser != null){
            return loginUser.getUserId();
        }
        return null;
    }

}
