package com.wuhao.email.Enum;
public enum EmailCode {
    EMAIL_SEND_SUCCESS(100,"邮件发送成功！"),
    EMAIL_MESSAGE_NULL(101,"发送的email（不能）为空"),
    EMAIL_MESSAGE_TO_NULL(102,"发送的email的收件人（不能）为空"),
    EMAIL_MESSAGE_SUBJECT_NULL(103,"发送的email的 主题（不能）为空,且不能超过128个字"),
    EMAIL_MESSAGE_TEXTANDMT_NULL(104,"发送的email的正文和附件信息不能同时为空"),


    LOGIN_INFO_SUCCESS(200,"登录成功"),
    LOGIN_INFO_ERROR(201,"用户名或者密码输入错误"),
    LOGIN_USERNAME_NULL(202,"用户未输入用户名或输入用户名过长"),
    LOGIN_USERPASSWORD_NULL(203,"用户未输入密码或输入用户名过长"),

    LOGIN_BY_EMAIL(250,"用户使用email登录"),
    LOGIN_BY_PHONE(251,"用户使用手机号登录"),
    LOGIN_BY_USERNAME(252,"用户使用用户名登录"),
    ;
    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    EmailCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
