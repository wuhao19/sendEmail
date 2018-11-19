package com.wuhao.email.domain;

import lombok.Data;

@Data
public class ResultMode {
    public static final int REGISTER_ERROR=1;
    public static final int REGISTER_SUCCESS=0;

    /**
     *  0表示成功 1表示失败
     */
    private int code=REGISTER_ERROR;

    /**
     * 记录注册的信息
     */
    private String message="";

    public ResultMode(){
        this.code = REGISTER_SUCCESS;
        this.message = "操作成功！";
    }

    public ResultMode(String message){
        this.code = REGISTER_ERROR;
        this.message = message;
    }
}
