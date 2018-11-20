package com.wuhao.email.vo;

import com.wuhao.email.domain.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrderVo {
    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 用户名
     */
    private String userName ;

    /**
     * 用户的手机号
     */
    private String userPhone;
    /**
     * 用户的地址列表
     */
    private String userAddress;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 支付类型 0到付，1支付宝，2微信支付
     */
    private Integer payType;

    /**
     * 订单明细id
     */
    private List<OrderDetail> orderDetailList;

    /**
     * 订单总金额
     */
    private BigDecimal orderTotal;

    /**
     * 订单创建时间
     */
    private LocalDateTime crateTime;

    /**
     * 订单创建人
     */
    private Integer creatBy;

}
