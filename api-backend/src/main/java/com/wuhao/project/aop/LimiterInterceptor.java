package com.wuhao.project.aop;


import com.wuhao.project.annotation.AuthCheck;
import com.wuhao.project.annotation.Limiter;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.support.sensitive.TrafficLimiter;
import com.wuhao.project.support.sensitive.algorithm.SlidingTimeWindowLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Aspect
@Component
public class LimiterInterceptor {
    private TrafficLimiter trafficLimiter = new SlidingTimeWindowLimiter();

    @Around("@annotation(limiter)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, Limiter limiter) throws Throwable {
        if(trafficLimiter.limit()){
            return Result.error(ErrorCode.CURRENT_REQUEST_MANY);
        }
        return joinPoint.proceed();
    }
}
