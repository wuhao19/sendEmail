package com.wuhao.email.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuhao.email.Enum.ProductStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;

    /**
     * 商品的名称
     */
    private String productName;

    /**
     * 商品的简单描述
     */
    private String productDescription;

    /**
     * 商品的详情表
     */
    private String productDetails;

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

    /**
     * 商品的状态 1为下架 ，0为上架 默认为1下架
     */
    private Integer productStatus=ProductStatusEnum.DOWN.getProductStatusCode();

    /**
     * 创建时间
     */
    private LocalDateTime crateTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Integer crateBy;

    /**
     * 更新人
     */
    private Integer updateBy;


}
