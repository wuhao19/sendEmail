package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Order;
import com.wuhao.email.domain.User;
import com.wuhao.email.excptionHandler.MyException;
import com.wuhao.email.mapper.OrderMapper;
import com.wuhao.email.service.IOrderService;
import com.wuhao.email.util.OrderUtils;
import org.apache.commons.lang3.StringUtils;
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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order crateOrder(User user,String userAddress) {

        if (user==null||StringUtils.isBlank(userAddress)){
            return null;
        }
        Order order=new Order();
        order.setId(OrderUtils.InitOrderId());//设置订单编号
        order.setUserName(user.getUserName());
        order.setUpdatBy(user.getUserId());
        order.setCreatBy(user.getUserId());
        order.setUserAddress(userAddress);
        order.setUserPhone(user.getUserPhone());
        order.setPayStatus(0);//默认未支付
        order.setOrderStatus(0);//未支付状态
        order.setPayType(0);//默认货到付款
        order.setOrderTotal(new BigDecimal(0));
        int insert = orderMapper.insert(order);
        if (insert==0){
            throw new MyException(123,"订单插入数据库失败");
        }
        return order;
    }

    /**
     * 货到付款处理
     * @param orderId
     * @return
     */
    @Override
    public boolean CashOnDelivery(String orderId,User user) {
        if (StringUtils.isBlank(orderId)||user==null){
            return false;
        }
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("id", orderId));
        if (order==null){
            return false;
        }
        if (order.getPayStatus()!=0||order.getOrderStatus()!=0){
            return false;
        }
        order.setPayType(0);//到付
        order.setOrderStatus(1);//订单状态为已支付
        order.setPayStatus(0);//订单实际未支付，到付完成后状态改为已支付
        order.setUpdatBy(user.getUserId());
        int result = orderMapper.update(order, new QueryWrapper<Order>().eq("id", orderId));
        if (result==0){
            return false;
        }
        return true;
    }

    /**
     * 用户进行取消订单操作
     * @param orderId
     * @param user
     * @return
     */
    @Override
    public boolean cancelOrder(String orderId,User user) {
        if (StringUtils.isBlank(orderId)){
            return false;
        }
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("id", orderId));
        if (order==null){
            return false;
        }
        order.setOrderStatus(2);//订单状态设置为2取消
        order.setUpdatBy(user.getUserId());
        int result = orderMapper.update(order, new QueryWrapper<Order>().eq("id", orderId));
        if (result==0){
            return false;
        }
        return true;
    }
}
