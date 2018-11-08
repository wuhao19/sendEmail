package com.wuhao.email.domain;

import lombok.Data;

@Data
public class LoginMode {
    public static final int SUCCESS=0;
    public static final int ERROR=-1;
    public static final int LOGIN_TYPE_ACCESS=0;
    public static final int LOGIN_TYPE_EMAIL=1;
    public static final int LOGIN_TYPE_PHONE=2;
    /***
     * 0 正常成功登陆  -1表示登陆失败
     */
    private int code =SUCCESS;

    /***
     * 错误异常
     */
    private String message ="";

    /**
     * 登陆类型
     */
    private int loginType = LOGIN_TYPE_ACCESS;


    public  LoginMode(){
        this.code = SUCCESS;
        this.message = "";
    }

    public  LoginMode(int loginType){
        this.code =SUCCESS;
        this.loginType = loginType;
        this.message ="";
    }


    public  LoginMode(String msg){
        this.code =ERROR;
        this.message =msg;
    }

}
