package com.wuhao.project.util;

import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.response.LoginUserResponse;
import org.springframework.beans.BeanUtils;

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
}
