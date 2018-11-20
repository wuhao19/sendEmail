package com.wuhao.email.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarVo {

    private Integer productId;

    private Integer productNum;

    private BigDecimal productPriceCount;
    /**
     * 商品的名称
     */
    private String productName;

    /**
     * 商品的简单描述
     */
    private String productDescription;

    /**
     * 商品的首图
     */
    private String productIcon;

    /**
     * 商品的单价
     */
    private BigDecimal productPrice ;

    /**
     * 商品的类型编号
     */
    private Integer categoryType;

}
