package com.wuhao.email.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public class OrderMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户名
     */
    private Integer userId;

    /**
     * 用户联系电话
     */
    private String userPhone;

    /**
     * 支付状态 0未支付 1已支付
     */
    private Integer payStatus;

    /**
     * 支付类型 0到付，1支付宝，2微信支付
     */
    private Integer payType;

    /**
     * 订单地址
     */
    private String userAddress;

    /**
     * 订单状态 0待支付 1已支付,待发货 2已取消 3已发货 4已完成 5已取消,退款中 6已取消,已退款 7已作废
     */
    private Integer orderStatus;

    /**
     * 订单总金额
     */
    private BigDecimal orderTotal;

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

    @TableField(exist = false)
    private List<OrderDetail> orderDetailList;


}
