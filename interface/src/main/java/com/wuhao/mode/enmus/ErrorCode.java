package com.wuhao.mode.enmus;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public enum  ErrorCode {
    PARAM_ERROR(50001,"参数"),
    ILLEGAL_ACCESS(50000,"非法访问,亲自重！！！"),
    CURRENT_REQUEST_MANY(50002,"请求人数过多，请稍后再试");
    ;
    private Integer code;
    private String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
