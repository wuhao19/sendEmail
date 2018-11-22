package com.wuhao.email.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuhao.email.domain.*;
import com.wuhao.email.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/order")
public class UserOrderController {
    @Value("${myConfig.ORDER_NULL_ERROR}")
    private String ORDER_NULL_ERROR;
    @Autowired
    private IOrderMasterService orderMasterService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IAddressService addressService;
    @Autowired
    private ICarService carService;
    @Autowired
    private IProductService productService;

    /**
     * 用户根据页数和用户的登录信息查询订单
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @PostMapping("/showOrder")
    public ApiMode showOrder(HttpServletRequest request,
                             @RequestParam(value = "page",defaultValue = "1")int page){
        User user = (User) request.getSession().getAttribute("user");
        //根据用户和传值的page进行查询用户的订单
        ApiMode apiMode;
        IPage<OrderMaster> orderMasterIPage = orderMasterService.findAllOrder(user, page);
        if (orderMasterIPage==null){
            apiMode = new ApiMode(ORDER_NULL_ERROR);
            return apiMode;
        }else {
         apiMode=new ApiMode();
            List<OrderMaster> allOrder = orderMasterIPage.getRecords();
            long current = orderMasterIPage.getCurrent();
            long total = orderMasterIPage.getTotal();
            long pages = orderMasterIPage.getPages();
            Map<String,Object> map = new HashMap<>();
            map.put("allOrder",allOrder);
            map.put("current",current);
            map.put("total",total);
            map.put("pages",pages);
            apiMode.setDate(map);
            return apiMode;
        }
    }

    /**
     * 处理用户的支付请求
     * @param request
     * @param orderId
     * @param payType
     * @return
     */
    @ResponseBody
    @PostMapping("/goPay")
    public ApiMode goPay(HttpServletRequest request,
                         @RequestParam(value = "orderId") String orderId,
                         @RequestParam(value = "payType") int payType){
        User user =(User) request.getSession().getAttribute("user");
        if (StringUtils.isBlank(orderId)||user==null){
            return new ApiMode("订单编号或者操作的用户不能为空哦");
        }
        ApiMode apiMode = orderMasterService.goPay(orderId, payType, user);
        if (apiMode==null){
            return new ApiMode("支付失败");
        }
       return apiMode;
    }

    /**
     * 查询一个订单
     * @param request
     * @param orderId
     * @return
     */
    @ResponseBody
    @PostMapping("/findOneOrder")
    public ApiMode findOneOrder(HttpServletRequest request,
                                @RequestParam(value = "orderId") String orderId
                                ){
        User user =(User) request.getSession().getAttribute("user");
        if (StringUtils.isBlank(orderId)||user==null){
            return new ApiMode("订单编号或者操作的用户不能为空哦");
        }
        OrderMaster orderMaster = orderMasterService.findOneOrder(orderId);
       if (orderMaster==null){
           return new ApiMode("未找到您要支付的订单");
       }
       List<OrderDetail> orderDetailList = orderDetailService.findOneOrderMasterDetail(orderId);
       if (orderDetailList.size()==0){
           return new ApiMode("该订单没有任何的商品可以购买");
       }
       orderMaster.setOrderDetailList(orderDetailList);
       ApiMode apiMode = new ApiMode();
       Map<String,Object> map = new HashMap<>();
       map.put("orderMaster",orderMaster);
       apiMode.setDate(map);
       return apiMode;
    }

    /**
     * 取消订单
     * @param request
     * @param orderId
     * @return
     */
    @ResponseBody
    @PostMapping("/cancelOrder")
    public ApiMode cancelOrder(HttpServletRequest request,
                               @RequestParam(value = "orderId")String orderId){
        User user =(User) request.getSession().getAttribute("user");
        if (StringUtils.isBlank(orderId)||user==null){
            return new ApiMode("订单编号或者操作的用户不能为空哦");
        }
        if (!orderMasterService.cancelOrder(orderId, user)) {
            return new ApiMode("取消订单失败");
        }
        return new ApiMode();
    }

    /**
     * 创建订单
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/crateOrder")
    public ApiMode crateOrder(HttpServletRequest request){
       User user = (User) request.getSession().getAttribute("user");
       if (user==null){
           return new ApiMode("用户必须登录！，创建订单失败");
       }
        List<Address> addressList = addressService.findAddressByUserId(user.getUserId());
        ApiMode apiMode = new ApiMode();
        if (addressList.size()==0||addressList==null){
            apiMode.setMessage("当前用户没有还没有地址哦，地址栏进行添加");
            apiMode.setCode(2);//无地址
        }
        String userAddress="";
       for (Address address:addressList){
           if (address.getAddressType()==1){
                userAddress = address.getAddress();
                break;
           }
       }
        if (userAddress.equals("")){
            userAddress = "请添加收货地址";
        }
        OrderMaster orderMaster = orderMasterService.crateOrder(user, userAddress);
        if (orderMaster==null){
            return new ApiMode("订单模板创建失败");
        }
        //给订单添加详情
        List<Car> carList = carService.findCarByUserId(user.getUserId());
        if (carList.size()==0){
            return new ApiMode("订单创建失败，购物车中并未添加商品");
        }
        BigDecimal orderTotal = new BigDecimal(0);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Car car :carList){
            Product product = productService.findProductById(car.getPid(), car.getPNum());
            OrderDetail orderDetail = orderDetailService.crateOrderDetail(product,car.getPNum(),user,orderMaster);
            if (orderDetail==null){
                return new ApiMode("订单详情创建失败啦");
            }
            orderDetailList.add(orderDetail);
            orderTotal =  orderTotal.add(orderDetail.getProductTotal());
            if (!carService.clearCar(product.getProductId(),user.getUserId())) {
                return new ApiMode("创建订单时，删除购物车内容失败啦");
            }
        }
        orderMaster.setOrderDetailList(orderDetailList);
        orderMaster.setOrderTotal(orderTotal);
        if (!orderMasterService.updateOrder(orderMaster)) {
            return new ApiMode("订单创建失败");
        }
        Map<String,Object> map =new HashMap<>();
        map.put("orderMaster",orderMaster);
        apiMode.setDate(map);
        return apiMode;
    }

    /**
     * 查找用户的订单数量
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/findOrderCount")
    public ApiMode findOrderCount(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return new ApiMode("用户只有登录才能查看订单数量");
        }
        int orderCount = orderMasterService.findOrderCountByUserId(user.getUserId());
        Map<String,Object> map = new HashMap<>();
        map.put("orderCount",orderCount);
        ApiMode apiMode = new ApiMode();
        apiMode.setDate(map);
        return apiMode;
    }
}
