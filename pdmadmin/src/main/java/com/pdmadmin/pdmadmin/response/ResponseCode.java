package com.pdmadmin.pdmadmin.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode{
    SUCCESS(200,"操作成功"),
    ERROR(500,"操作失败"),
    CREATE_CAPTCHA_ERROR(2001, "生成验证码失败"),
    /**
     * 用户名密码错误
     */
    USERNAME_USERPWD_ERROR(1002, "用户名密码错误"),
    /**
     * 验证码错误
     */
    CAPTCHA_ERROR(1003, "验证码错误"),
    USERNAME_EXIST(1001,"用户已存在");

    private Integer code;
    private String message;
}
