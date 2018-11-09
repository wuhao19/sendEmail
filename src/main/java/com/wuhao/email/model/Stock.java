package com.wuhao.email.model;

import lombok.Data;

import java.util.Date;

@Data
public class Stock {

    //库存的id
    private Integer stockId;

    //商品的id
    private Integer productId;

    //对应库存的数量
    private Integer stockNumber;

    // 商品的创建时间
    private Date crateTime;

    // 商品的更新时间
    private Date updateTime;

    // 商品的创建人
    private String founder;

    // 商品的更新人
    private String modifier;

}
