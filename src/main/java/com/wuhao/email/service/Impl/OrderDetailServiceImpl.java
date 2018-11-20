package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Order;
import com.wuhao.email.domain.OrderDetail;
import com.wuhao.email.domain.Product;
import com.wuhao.email.domain.User;
import com.wuhao.email.excptionHandler.MyException;
import com.wuhao.email.mapper.OrderDetailMapper;
import com.wuhao.email.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-20
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public OrderDetail crateOrderDetail(Product product, int productNum, User user, Order order) {
        if (product==null||productNum==0||user==null||order==null){
            return null;
        }
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProductName(product.getProductName());
        orderDetail.setProductNum(productNum);
        orderDetail.setProductIcon(product.getProductIcon());
        orderDetail.setProductPrice(product.getProductPrice());
        orderDetail.setProductTotal(product.getProductPrice().multiply(new BigDecimal(productNum)));
        orderDetail.setOrderId(order.getId());
        orderDetail.setCreatBy(user.getUserId());
        orderDetail.setUpdatBy(user.getUserId());
        int insert = orderDetailMapper.insert(orderDetail);
        if (insert==0){
            throw new MyException(123,"订单详情插入失败");
        }
        return orderDetail;
    }
}
