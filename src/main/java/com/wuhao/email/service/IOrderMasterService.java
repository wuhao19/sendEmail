package com.wuhao.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.ApiMode;
import com.wuhao.email.domain.OrderMaster;
import com.wuhao.email.domain.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-20
 */
public interface IOrderMasterService extends IService<OrderMaster> {
    OrderMaster crateOrder(User user, String userAddress);

    ApiMode goPay(String orderId, int payType, User user);//支付服务

    ApiMode cancelOrder(String orderId,User user);

    List<OrderMaster> findAllOrder(User user, int current);

    ApiMode findOneOrder(String orderId);
}
