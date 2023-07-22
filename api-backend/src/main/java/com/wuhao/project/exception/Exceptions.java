package com.wuhao.project.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

//@RestControllerAdvice
public class Exceptions {

    @ExceptionHandler(Exception.class)
    public void doException(Exception e){
        System.out.println(666);
    }
}