package com.wuhao.project.common;

public enum ErrorCode {

    SUCCESS(0, "ok"),
    USER_ACCOUNT_PASSWORD(40000,"账号或者密码错误"),
    USER_ACCOUNT_NULL(40001,"账号为空"),
    USER_PASSWORD_NULL(40002,"密码为空"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    USER_STATUS_ERROR(40102,"账号状态异常"),
    USER_STATUS_ONE(40103,"此账号已被封禁"),
    USER_STATUS_TWO(40104,"此账号已被注销"),
    USER_REGISTER_ERROR(4103,"注册失败"),
    ALREADY_REGISTER(4104,"已注册"),
    ERROR_CODE(4105,"验证码错误"),
    COMMENT_NULL(4106,"评论内容不可以为空"),
    COMMENT_ILLEGAL(4107,"评论中有敏感词,请重新输入"),


    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    DELETE_ERROR(50002,"删除失败"),
    UPDATE_ERROR(50003,"修改失败"),
    INSERT_ERROR(50004,"添加失败"),
    PARAMS_ERROR(40004, "请求参数异常"),
    PARAMS_NULL(40000, "请求参数为空"),
    NO_AUTH_ERROR(50005, "无权限操作"),
    NOT_FOUND_ERROR(50006, "请求数据不存在"),
    CURRENT_REQUEST_MANY(50007,"当前请求人数过多，请稍后在试");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
