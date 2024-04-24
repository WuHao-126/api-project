package com.wuhao.aop;



import com.wuhao.annotation.Limiter;
import com.wuhao.common.Result;
import com.wuhao.mode.enmus.ErrorCode;
import com.wuhao.support.sensitive.TrafficLimiter;
import com.wuhao.support.sensitive.algorithm.SlidingTimeWindowLimiter;
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

    @Around("execution(* com.wuhao.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        if(trafficLimiter.limit()){
            return Result.error(ErrorCode.CURRENT_REQUEST_MANY);
        }
        return joinPoint.proceed();
    }
}
