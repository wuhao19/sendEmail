package com.wuhao.email.controller;

import com.wuhao.email.domain.Product;
import com.wuhao.email.domain.User;
import com.wuhao.email.excptionHandler.MyException;
import com.wuhao.email.service.ICarService;
import com.wuhao.email.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopIndexController {
    @Value("${myConfig.CAR_URL}")
    private String CAR_URL;
    @Value("${myConfig.REGISTER_URL}")
    private String REGISTER_URL;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICarService carService;

    @RequestMapping("/listProduct")
    public String listProduct(Model model,
                              HttpServletRequest request){
        // 查询所有的上架的商品
        List<Product> productList = productService.listAllProduct();
        if (productList==null){
            throw new MyException("没有任何商品上架");
        }
        // 查询当前是否用用户登录
        User user =(User) request.getSession().getAttribute("user");
        int carCount=0;
        if (user!=null){
            carCount = carService.findCarCount(user.getUserId());
        }
        // 返回所有商品进行展示在前端
        model.addAttribute("productList",productList);
        model.addAttribute("user",user);
        model.addAttribute("carCount",carCount);
        model.addAttribute("carUrl",CAR_URL);
        model.addAttribute("registerUrl",REGISTER_URL);
        return "index";
    }




}
