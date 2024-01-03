package com.wuhao.project.exception;


import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(com.wuhao.project.exception.BusinessException.class)
    public Result businessExceptionHandler(com.wuhao.project.exception.BusinessException e) {
        log.error("BusinessException", e);
//        return Result.error(e.getCode(), e.getMessage());
        return null;
    }

    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return Result.error(ErrorCode.SYSTEM_ERROR);
    }
}