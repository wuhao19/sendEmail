package com.wuhao.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.OrderDetail;
import com.wuhao.email.domain.OrderMaster;
import com.wuhao.email.domain.Product;
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
public interface IOrderDetailService extends IService<OrderDetail> {

    OrderDetail crateOrderDetail(Product product, int productNum, User user, OrderMaster order);

    List<OrderDetail> findOneOrderMasterDetail(String orderId);
}
