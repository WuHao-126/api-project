package com.wuhao.common;

import com.wuhao.mode.enmus.ErrorCode;
import lombok.Data;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class Result {
    private Integer code;
    private Object data;
    private String message;

    public Result(){
        this.code=code;
    }
    public Result(Integer code){
        this.code=code;
    }
    public Result(Integer code,Object data){
        this.code=code;
        this.data=data;
    }
    public Result(Integer code,Object data,String message){
        this.code=code;
        this.data=data;
        this.message=message;
    }
    public  static Result success(){
        return new Result(0,null,null);
    }
    public  static Result success(Object data){
        return new Result(0,data,null);
    }
    public  static Result success(Object data,String message){
        return new Result(0,data,message);
    }

    public  static Result error(Integer code,String message){
        return new Result(code,null,message);
    }
    public  static Result error(ErrorCode errorCode){
        return new Result(errorCode.getCode(),null,errorCode.getMessage());
    }
    public  static Result error(Integer code,ErrorCode errorCode){
        return new Result(code,null,errorCode.getMessage());
    }
    public  static Result error(String message,ErrorCode errorCode){
        return new Result(errorCode.getCode(),null,message);
    }

}
