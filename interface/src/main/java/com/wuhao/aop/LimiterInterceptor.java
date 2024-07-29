package com.wuhao.aop;



import com.wuhao.annotation.Limiter;
import com.wuhao.common.Result;
import com.wuhao.mode.enmus.ErrorCode;
import com.wuhao.support.sensitive.TrafficLimiter;
import com.wuhao.support.sensitive.algorithm.SlidingTimeWindowLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Aspect
@Component
public class LimiterInterceptor {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TrafficLimiter trafficLimiter;

    @Around("execution(* com.wuhao.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().getName();
        if(!trafficLimiter.allowRequest(name)){
            return Result.error(ErrorCode.CURRENT_REQUEST_MANY);
        }
        return joinPoint.proceed();
    }
}
