package com.wuhao.email.domain;

import lombok.Data;

import java.util.Map;
@Data
public class ApiMode {
    /**
     * 错误码 0为操作正常 其他的为操作错误
     */
    private int code=0;
    /**
     * 错误消息 空为正常 其他为发生错误
     */
    private String message="";
    /**
     * 需要传递的数据封装
     */
    private Map<String,Object> date;

    public ApiMode(String message){
        this.message=message;
        this.code=1;
    }
    public ApiMode(){
    }
}
