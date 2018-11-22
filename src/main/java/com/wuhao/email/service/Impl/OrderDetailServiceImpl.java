package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.OrderDetail;
import com.wuhao.email.domain.OrderMaster;
import com.wuhao.email.domain.Product;
import com.wuhao.email.domain.User;
import com.wuhao.email.mapper.OrderDetailMapper;
import com.wuhao.email.service.IOrderDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    /**
     * 重购物车新建订单
     * @param product
     * @param productNum
     * @param user
     * @param order
     * @return
     */
    @Override
    public OrderDetail crateOrderDetail(Product product, int productNum, User user, OrderMaster order) {
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
        orderDetail.setPid(product.getProductId());
        int insert = orderDetailMapper.insert(orderDetail);
        if (insert==0){
           return null;
        }
        return orderDetail;
    }

    /**
     * 查询同一个订单的订单详情
     * @param orderId
     * @return
     */
    @Override
    public List<OrderDetail> findOneOrderMasterDetail(String orderId) {
       if (StringUtils.isBlank(orderId)){
           return null;
       }
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(new QueryWrapper<OrderDetail>().eq("order_id", orderId));
       if (orderDetailList.size()==0){
           return null;
       }
       return orderDetailList;
    }
}
