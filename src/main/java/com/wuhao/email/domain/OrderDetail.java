package com.wuhao.email.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wuhao
 * @since 2018-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单明细主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 对应的订单id string类型
     */
    private String orderId;

    /**
     * 商品的id
     */
    private Integer pid;

    /**
     * 商品的名称
     */
    private String productName;
    /**
     * 商品的图片
     */
    private String productIcon;

    /**
     * 商品单价
     */
    private BigDecimal productPrice;

    /**
     * 商品的数量
     */
    private Integer productNum;

    /**
     * 该商品合计
     */
    private BigDecimal productTotal;

    /**
     * 订单创建时间
     */
    private LocalDateTime crateTime;

    /**
     * 订单更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 订单创建人
     */
    private Integer creatBy;

    /**
     * 订单更新人
     */
    private Integer updatBy;


}
