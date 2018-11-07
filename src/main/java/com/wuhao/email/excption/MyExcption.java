package com.wuhao.email.excption;


import com.wuhao.email.Enum.EmailCode;

public class MyExcption extends RuntimeException{
    private Integer code;
    public MyExcption(EmailCode emailCode){
        super(emailCode.getMessage());
        this.code=emailCode.getCode();
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
