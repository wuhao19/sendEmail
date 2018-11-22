package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.ApiMode;
import com.wuhao.email.domain.OrderMaster;
import com.wuhao.email.domain.User;
import com.wuhao.email.mapper.OrderMasterMapper;
import com.wuhao.email.service.IOrderMasterService;
import com.wuhao.email.util.OrderUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class OrderMasterServiceImpl extends ServiceImpl<OrderMasterMapper, OrderMaster> implements IOrderMasterService {
    @Value("${myConfig.ORDER_PAGE_SIZE}")
    private int ORDER_PAGE_SIZE;

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    /**
     * 创建一个新的订单模板
     * @param user
     * @param userAddress
     * @return
     */
    @Override
    public OrderMaster crateOrder(User user, String userAddress) {
        if (user==null||StringUtils.isBlank(userAddress)){
            return null;
        }
        OrderMaster order=new OrderMaster();
        order.setId(OrderUtils.InitOrderId());//设置订单编号
        order.setUserName(user.getUserName());
        order.setUpdatBy(user.getUserId());
        order.setCreatBy(user.getUserId());
        order.setUserAddress(userAddress);
        order.setUserPhone(user.getUserPhone());
        order.setPayStatus(0);//默认未支付
        order.setOrderStatus(0);//未支付状态
        order.setPayType(0);//默认货到付款
        order.setUserId(user.getUserId());
        order.setOrderTotal(new BigDecimal(0));
        int insert = orderMasterMapper.insert(order);
        if (insert==0){
            return null;
        }
        return order;
    }

    /**
     * 货到付款处理
     * @param orderId
     * @return
     */
    @Override
    public ApiMode goPay(String orderId, int payType, User user) {
      if (StringUtils.isBlank(orderId)||user==null){
          return new ApiMode("订单编号或者用户不能为空") ;
      }
      ApiMode apiMode=null;
      switch (payType){
          case 0://货到付款
              apiMode = CashOnDelivery(orderId, user);
              break;
          case 1://支付宝支付

              break;
          case 2://微信支付

              break;
              default:
                  apiMode= new ApiMode("支付的类型不符合，亲重新检查");
                  break;
      }
      return apiMode;
    }

    /**
     * 货到付款
     * @param orderId
     * @return
     */
    private ApiMode CashOnDelivery(String orderId,User user){
        OrderMaster orderMaster = orderMasterMapper.selectOne(new QueryWrapper<OrderMaster>().eq("id", orderId));
        if (orderMaster==null){
            return new ApiMode("未在数据库中找到订单") ;
        }
        if (orderMaster.getPayStatus()==1){
            return new ApiMode("这个订单已经支付了，不要重复支付");
        }
        if (orderMaster.getOrderStatus()==0&&orderMaster.getPayType()==0){
            orderMaster.setPayStatus(1);
            orderMaster.setOrderStatus(1);
            orderMaster.setUpdatBy(user.getUserId());
            int result = orderMasterMapper.update(orderMaster, new QueryWrapper<OrderMaster>().eq("id", orderMaster.getId()));
            if (result==1){
                return new ApiMode();
            }
            return new ApiMode("在修改订单状态时失败啦，请稍后再试");
        }else {
            return new ApiMode("该订单的状态不对，下单失败");
        }
    }

    /**
     * 用户进行取消订单操作
     * @param orderId
     * @param user
     * @return
     */
    @Override
    public boolean cancelOrder(String orderId,User user) {
        if (StringUtils.isBlank(orderId)||user==null){
            return false;
        }
        OrderMaster orderMaster = orderMasterMapper.selectOne(new QueryWrapper<OrderMaster>().eq("id",orderId).eq("user_id",user.getUserId()));
        if (orderMaster==null){
           return false;
        }
        orderMaster.setUpdatBy(user.getUserId());
        if (orderMaster.getOrderStatus()==2){
            return false;
        }
        if (orderMaster.getPayStatus()==1){//已经支付
            if(orderMaster.getPayType()==0){//货到付款
              return   changeOrderStatus(orderMaster,2);
            }else {//在线支付
              return   changeOrderStatus(orderMaster,5);
            }
        }else {//未支付 直接修改为 取消状态
          return   changeOrderStatus(orderMaster,2);
        }
    }

    /**
     * 处理用户的订单状态
     * @param orderMaster
     * @param payTypeCode
     * @return
     */
    private boolean changeOrderStatus(OrderMaster orderMaster,int payTypeCode){
       orderMaster.setOrderStatus(payTypeCode);
       int result = orderMasterMapper.update(orderMaster, new QueryWrapper<OrderMaster>().eq("id", orderMaster.getId()));
        if (result==0){
            return false;
        }
        return true;
    }

    /**
     * 分页查询用户的所有订单
     * @param user
     * @param current
     * @return
     */
    @Override
    public IPage<OrderMaster> findAllOrder(User user, int current) {
        if (user==null){return null;}
        Page<OrderMaster> page = new Page<>(current,ORDER_PAGE_SIZE);
        IPage<OrderMaster> orderMasterIPage = orderMasterMapper.selectPage(page,new QueryWrapper<OrderMaster>().eq("user_id",user.getUserId()));
        if (orderMasterIPage==null){
            return null;
        }
        return orderMasterIPage;
    }

    /**
     * 查询一个订单
     * @param orderId
     * @return
     */
    @Override
    public OrderMaster findOneOrder(String orderId) {
        if (StringUtils.isBlank(orderId)){
            return null;
        }
        OrderMaster orderMaster = orderMasterMapper.selectOne(new QueryWrapper<OrderMaster>().eq("id", orderId));
        if (orderMaster==null){
            return null;
        }
        return orderMaster;
    }

    /**
     * 对订单进行更新
     * @param orderMaster
     * @return
     */
    @Override
    public boolean updateOrder(OrderMaster orderMaster) {
        if (orderMaster==null){
            return false;
        }
        int result = orderMasterMapper.update(orderMaster, new QueryWrapper<OrderMaster>().eq("id", orderMaster.getId()));
        if (result==0){
            return false;
        }
        return true;
    }

    @Override
    public int findOrderCountByUserId(int userId) {
        if (StringUtils.isBlank(String.valueOf(userId))){
            return 0;
        }
        return orderMasterMapper.selectCount(new QueryWrapper<OrderMaster>().eq("user_id", userId));
    }
}
