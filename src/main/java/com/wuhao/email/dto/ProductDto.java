package com.wuhao.email.dto;

import lombok.Data;

@Data
public class ProductDto {
//    /**
//     * 商品的单价
//     */
//    private BigDecimal productPrice ;

    /**
     * 商品的类型编号
     */
    private String categoryType;

    /**
     * 商品的状态 1为下架 ，0为上架 默认为1下架
     */
    private String productStatus;
    /**
     * 关键字
     */
    private String keyName;
    /**
     * 库存排序 0正序 1倒序
     */
    private String productStockSort;
    /**
     * 筛选的开始时间
     */
    private String starDate;
    /**
     * 筛选的截止时间
     */
    private String endDate;


}
