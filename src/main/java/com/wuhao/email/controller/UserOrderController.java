package com.wuhao.email.controller;

import com.wuhao.email.domain.ApiMode;
import com.wuhao.email.domain.OrderDetail;
import com.wuhao.email.domain.OrderMaster;
import com.wuhao.email.domain.User;
import com.wuhao.email.service.IOrderDetailService;
import com.wuhao.email.service.IOrderMasterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        List<OrderMaster> allOrder = orderMasterService.findAllOrder(user, page);
        if (allOrder.size()==0){
            apiMode = new ApiMode(ORDER_NULL_ERROR);
            return apiMode;
        }else {
         apiMode=new ApiMode();
         Map<String,Object> map = new HashMap<>();
         map.put("allOrder",allOrder);
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
        ApiMode apiMode = orderMasterService.findOneOrder(orderId);
       if (apiMode==null){
           return new ApiMode("未找到您要支付的订单");
       }
        List<OrderDetail> orderDetailList = orderDetailService.findOneOrderMasterDetail(orderId);
       if (orderDetailList==null){
           return new ApiMode("该订单没有任何的商品可以购买");
       }
       Map<String,Object> map = apiMode.getDate();
       map.put("orderDetailList",orderDetailList);
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
        ApiMode apiMode = orderMasterService.cancelOrder(orderId, user);
        if (apiMode==null){
            return new ApiMode("取消订单失败");
        }
        return apiMode;
    }

}
