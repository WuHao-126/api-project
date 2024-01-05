package com.wuhao.project.common;

import lombok.Data;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class Result{
    private int code;
    private Object data;
    private String message;

    public Result(int code,Object data,String message){
        this.code=code;
        this.data=data;
        this.message=message;
    }
    public static Result success(Object data){
         return new Result(0,data,null);
    }
    public static Result success(){
        return new Result(0,null,null);
    }
    public static Result error(int code,String message){
        return new Result(code,null,message);
    }
    public static Result error(ErrorCode errorCode){
        return new Result(errorCode.getCode(),null,errorCode.getMessage());
    }
}
