package com.wuhao.project.filter;


import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.config.IgnoreWhiteList;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.util.JwtUtil;
import com.wuhao.project.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        String requestURI = request.getRequestURI();
//        // 跳过不需要验证的路径
//        if (StringUtils.matches(requestURI, IgnoreWhiteList.getWhites())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String token = request.getHeader("authorization");
//        if(StringUtils.isNotEmpty(token)){
//            boolean flag = JwtUtil.validateToken(token);
//            if(flag){
//                throw new BusinessException(ErrorCode.USER_LOGIN_EXPIRE);
//            }
//        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
