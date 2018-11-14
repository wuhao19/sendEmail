package com.wuhao.email.excptionHandler;

import lombok.Data;

@Data
public class MyException extends RuntimeException{
    private int errorCode;
    private String errorMessage;

    public MyException(int errorCode,String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public MyException(String errorMessage){
        super(errorMessage);
    }
    public MyException(){}
}
