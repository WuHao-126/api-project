package com.wuhao.project.interceptor;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuhao.project.constant.RedisConstant;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.response.FirstTokenResponse;
import com.wuhao.project.model.response.LoginUser;
import com.wuhao.project.security.SecurityContextHolder;
import com.wuhao.project.service.UserService;
import com.wuhao.project.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Slf4j
@Component
public class RequestInterceptor implements AsyncHandlerInterceptor {

    /**
     * 这个方法在请求实际到达控制器之前被调用。它用于决定是否继续处理请求，或者是否直接返回。如果这个方法返回true，则请求将继续被处理；如果返回false，则Spring MVC将不再继续处理该请求，并且通常会立即返回。这可以用于权限检查、日志记录等。
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //如果 handler 不是 HandlerMethod，则返回 true，表示继续处理该请求。这通常适用于静态资源（如图片、CSS、JavaScript 文件）等不需要经过控制器处理的请求。
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = replaceTokenBeran(request.getHeader("authorization"));
        if(StringUtils.isNotEmpty(token)){
            Claims claims = JwtUtil.getClaims(token);
            if(claims.get("userId") != null){
                String userId = claims.get("userId").toString();
                log.info("本次登录用户是：{}，token为：{}",userId,token);
                RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
                RBucket<FirstTokenResponse> bucket = redissonClient.getBucket(RedisConstant.API_USER_TOKEN+userId);
                FirstTokenResponse user = bucket.get();
                if(user != null && token.equals(user.getToken())){
                    LoginUser loginUser = new LoginUser();
                    loginUser.setUserId(userId);
                    loginUser.setToken(token);
                    SecurityContextHolder.set("loginUser",loginUser);
                }
            }
        }
        return true;
    }



    /**
     * 这个方法在整个请求完成后被调用，即在视图被渲染之后。它可以用来进行资源清理工作，比如关闭数据库连接或者释放其他资源。这个方法的调用是在请求的整个生命周期结束时，无论请求是成功完成还是因为异常而结束。参数ex包含了请求处理过程中可能抛出的任何异常。
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContextHolder.remove();
    }

    /**
     * token截取
     * @param token
     * @return
     */
    public String replaceTokenBeran(String token){
        if(StringUtils.isNotEmpty(token)){
            return token.replace("Bearer ", "");
        }
        return "";
    }
}
