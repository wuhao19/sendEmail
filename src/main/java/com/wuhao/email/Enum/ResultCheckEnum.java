package com.wuhao.email.Enum;

public enum  ResultCheckEnum {


    ;
    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResultCheckEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
