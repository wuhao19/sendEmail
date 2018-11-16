package com.wuhao.email.Enum;


import lombok.Getter;

@Getter
public enum  ProductStatusEnum {
    UP(0,"上架"),
    DOWN(1,"下架")
    ;
    private int productStatusCode;
    private String productStatusMessage;

    ProductStatusEnum(int productStatusCode, String productStatusMessage) {
        this.productStatusCode = productStatusCode;
        this.productStatusMessage = productStatusMessage;
    }
}
