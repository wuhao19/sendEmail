package com.wuhao.email.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品信息
 */
@Data
public class ProductInfo {
    // 商品的Id
    private Integer productId;

    // 商品的名称
    private String productName;

    // 商品的简单描述
    private String productDescription;

    // 商品的详情表
    private String productDetails;

    // 商品是否上架？0是上架，1是下架 默认上架0
    private Integer productStatus=0;

    // 商品的单价
    private BigDecimal productPrice;

    // 商品的首图表组编号
    private Integer iconGroup;

    // 商品的库存表
    private Integer productStock=0;

    // 商品的评论组编号
    private Integer commentaryGroup=0;

    // 商品的类型表
    private Integer categoryType=0;

    // 商品的创建时间
    private Date crateTime;

    // 商品的更新时间
    private Date updateTime;

    // 商品的创建人
    private String founder="1";

    // 商品的更新人
    private String modifier;

}
