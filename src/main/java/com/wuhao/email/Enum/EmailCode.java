package com.wuhao.email.Enum;
public enum EmailCode {
    EMAIL_SEND_SUCCESS(100,"邮件发送成功！"),
    EMAIL_MESSAGE_NULL(101,"发送的email（不能）为空"),
    EMAIL_MESSAGE_TO_NULL(102,"发送的email的收件人（不能）为空"),
    EMAIL_MESSAGE_SUBJECT_NULL(103,"发送的email的 主题（不能）为空,且不能超过128个字"),
    EMAIL_MESSAGE_TEXTANDMT_NULL(104,"发送的email的正文和附件信息不能同时为空"),

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
