package com.wuhao.project.util;

import cn.hutool.core.util.StrUtil;

/**
 * @author 虎哥
 */
public class RegexUtils {
    /**
     * 是否是无效手机格式
     * @param phone 要校验的手机号
     * @return true:符合，false：不符合
     */
    public static boolean isPhoneInvalid(String phone){
        return mismatch(phone, RegexPatterns.PHONE_REGEX);
    }
    /**
     * 是否是无效邮箱格式
     * @param email 要校验的邮箱
     * @return true:符合，false：不符合
     */
    public static boolean isEmailInvalid(String email){
        return mismatch(email, RegexPatterns.EMAIL_REGEX);
    }

    /**
     * 是否是无效验证码格式
     * @param code 要校验的验证码
     * @return true:符合，false：不符合
     */
    public static boolean isCodeInvalid(String code){
        return mismatch(code, RegexPatterns.VERIFY_CODE_REGEX);
    }

    /**
     * 账号是否含有特殊字符
     * @param account 要验证的账户
     * @return
     */
    public static boolean isAccountInvalid(String account){
        return mismatch(account, RegexPatterns.ACCOUNT_REGEX);
    }

    /**
     * 密码是否含有特殊字符
     * @param password
     * @return
     */
    public static boolean isPasswordInvalid(String password){
        return mismatch(password,RegexPatterns.PASSWORD_REGEX);
    }
    // 校验是否不符合正则格式
    private static boolean mismatch(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return str.matches(regex);
    }
}
