package com.wuhao.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.Order;
import com.wuhao.email.domain.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-20
 */
public interface IOrderService extends IService<Order> {

    Order crateOrder(User user,String userAddress);

    boolean CashOnDelivery(String orderId,User user);//货到付款

    boolean cancelOrder(String orderId,User user);
}
