package com.wuhao.email.controller;


import com.wuhao.email.domain.*;
import com.wuhao.email.excptionHandler.MyException;
import com.wuhao.email.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wuhao
 * @since 2018-11-20
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Value("${myConfig.DO_PAY_URL}")
    private String DO_PAY_URL;
    @Value("${myConfig.CANCEL_ORDER_URL}")
    private String CANCEL_ORDER_URL;

    @Autowired
    private IOrderService orderService;
    @Autowired
    private ICarService carService;
    @Autowired
    private IAddressService addressService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IProductService productService;

    /**
     * 展示订单并将订单保存到数据库
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/showOrder")
    public String showOrder(HttpServletRequest request, Model model){
        User user = (User)request.getSession().getAttribute("user");
        List<Car> carList = carService.findCarByUserId(user.getUserId());
        String userAddress = addressService.findAddressByUserId(user.getUserId());
        Order order = orderService.crateOrder(user,userAddress);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        BigDecimal orderTotal=new BigDecimal(0);
        for (Car car:carList){
            Product product = productService.findProductById(car.getPid(),0);
            OrderDetail o = orderDetailService.crateOrderDetail(product,car.getPNum(),user,order);
            orderTotal= orderTotal.add(o.getProductTotal());
            orderDetailList.add(o);
        }
        order.setOrderTotal(orderTotal);
        //order.setOrderDetails(orderDetailList);
        model.addAttribute("order",order);
        model.addAttribute("doPayUrl",DO_PAY_URL);
        model.addAttribute("cancelOrder",CANCEL_ORDER_URL);
        return "order/order";
    }

    /**
     * 处理用户提交的支付订单
     * @param payType
     * @param orderId
     * @return
     */
    @GetMapping("doPay")
    public String doPay(@RequestParam String payType,
                        @RequestParam String orderId,
                        HttpServletRequest request
                        ){
        User user =(User) request.getSession().getAttribute("user");
        if (StringUtils.isBlank(payType)||StringUtils.isBlank(orderId)||user==null){
            throw new MyException(123,"支付出现错误");
        }
        //判断支付的类型
        boolean payFlag=false;
        switch (payType){
            case "0": //货到付款
                payFlag = orderService.CashOnDelivery(orderId,user);
                break;
            case "1": //支付宝

                break;
            case "2": //微信支付

                break;
        }
        if (!payFlag){
            throw new MyException(123,"支付失败");
        }
        return "order/userOrder";
    }

    /**
     * 取消订单
     * @param orderId
     * @param request
     * @return
     */
    @GetMapping("cancelOrder")
    public String cancelOrder(@RequestParam String orderId,
                              HttpServletRequest request){
        User user =(User)request.getSession().getAttribute("user");
        if (StringUtils.isBlank(orderId)||user==null){
            throw  new MyException(123,"订单取消失败，未找到订单编号");
        }

        boolean result = orderService.cancelOrder(orderId, user);
        if (!result){
            throw new MyException(123,"订单取消失败");
        }
        return "order/userOrder";
    }


}
