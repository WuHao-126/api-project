package com.wuhao.mode.enmus;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public enum  ErrorCode {
    PARAM_ERROR(50001,"参数")
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
