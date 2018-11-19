package com.wuhao.email.controller;


import com.wuhao.email.domain.*;
import com.wuhao.email.excptionHandler.MyException;
import com.wuhao.email.service.ICarService;
import com.wuhao.email.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wuhao
 * @since 2018-11-19
 */
@Controller
@RequestMapping("/car")
public class CarController {
    @Value("${myConfig.REDIRECT_URL}")
    private static String REDIRECT_URL;

    @Autowired
    private IProductService productService;
    @Autowired
    private ICarService carService;
    @ResponseBody
    @PostMapping("/addCar")
    public ResultMode addCar(@RequestParam int productId,
                               @RequestParam int productNum,
                               HttpServletRequest request,
                               Model model){
        //获取用户id
        HttpSession session = request.getSession();
        User user =(User) session.getAttribute("user");
        if (user==null){
            return new ResultMode("请先进行登录");
        }
        //查询商品
        Product product = productService.findProductById(productId,productNum);
        if (product==null){
            throw new MyException(123,"没有查询到对应的商品");
        }
        //添加购物车列表
        if (!carService.addCar(user.getUserId(),productId,productNum)) {
            throw new MyException(123,"商品添加购物车失败");
        }
        Integer carCount = carService.findCarCount(user.getUserId());
        model.addAttribute("carCount",carCount);
        //返回添加结果
        return new ResultMode();
    }

    @RequestMapping("/showCar")
    public String showCar(HttpServletRequest request,
                          Model model){
        //获取当前用户的id
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        //进行数据库该用户的购物车查询
        List<Car> carList = carService.findCarByUserId(user.getUserId());
        List<Product> productList= new ArrayList<>();
        Product product;
        //遍历查询商品信息
        for (Car car : carList){
           product= productService.findProductById(car.getPid(),0);//0无任何作用 补充位置
            if (product!=null){
                productList.add(product);
            }
        }
        model.addAttribute("productList",productList);
        model.addAttribute("carList",carList);
        model.addAttribute("user",user);
        return "car/car";
    }
}
