package com.wuhao.project.security;

import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.response.LoginUser;

public class UserContextHolder {
    private static final ThreadLocal<LoginUser> userContext = new ThreadLocal<>();

    public static LoginUser getContext() {
        LoginUser context = userContext.get();
        if (context == null) {
            context = new LoginUser();
            userContext.set(context);
        }
        return context;
    }

    public static void setContext(LoginUser user) {
        userContext.set(user);
    }

    public static void clearContext() {
        userContext.remove();
    }
}