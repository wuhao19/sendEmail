package com.wuhao.email.Enum;

public enum ResultEnum {


    ;
    private int errorCode;
    private String errorMessage;
    public String getErrorMessage(){
        return errorMessage;
    }
    public int getErrorCode(){
        return errorCode;
    }

    ResultEnum(String errorMessage){
        this.errorMessage = errorMessage;
    }

}
