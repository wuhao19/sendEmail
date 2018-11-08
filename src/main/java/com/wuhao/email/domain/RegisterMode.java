package com.wuhao.email.domain;

import lombok.Data;

@Data
public class RegisterMode {
    public static final int REGISTER_ERROR=1;
    public static final int REGISTER_SUCCESS=0;

    /**
     *  0表示注册成功 1表示注册失败
     */
    private int code=REGISTER_ERROR;

    /**
     * 记录注册的信息
     */
    private String message="";

    public RegisterMode(){
        this.code = REGISTER_SUCCESS;
        this.message = "";
    }

    public RegisterMode(String message){
        this.code = REGISTER_ERROR;
        this.message = message;
    }
}
