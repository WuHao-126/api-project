package com.wuhao.aop;

import com.wuhao.common.Result;
import com.wuhao.mode.enmus.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
//@Aspect
//@Component
public class RequestInterceptor {
    private static final String headerKey="API-GATEWAY";
    /**
     * 执行拦截
     */
    @Around("execution(* com.wuhao.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 生成请求唯一 id
        String header = httpServletRequest.getHeader("X-AuthorizationToken-Header");
        if(StringUtils.isEmpty(header) || !headerKey.equals(header)){
            return Result.error(ErrorCode.ILLEGAL_ACCESS);
        }
        return point.proceed();
    }
}
