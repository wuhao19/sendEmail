package com.wuhao.email.controller;

import com.wuhao.email.domain.ApiMode;
import com.wuhao.email.domain.Car;
import com.wuhao.email.domain.Product;
import com.wuhao.email.domain.User;
import com.wuhao.email.service.ICarService;
import com.wuhao.email.service.IProductService;
import com.wuhao.email.vo.CarVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/car")
public class UserCarController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ICarService carService;

    @ResponseBody
    @PostMapping("/addCar")
    public ApiMode addCar(@RequestParam(value = "productId") int productId,
                          @RequestParam(value = "productNum") int productNum,
                          HttpServletRequest request){
        //获取用户id
        User user =(User) request.getSession().getAttribute("user");
        if (user==null|| StringUtils.isBlank(String.valueOf(productId))||productNum==0){
           return new ApiMode("用户和商品不能为空或者购买的商品不能为0件哦");
        }
        //查询商品
        Product product = productService.findProductById(productId,productNum);
        if (product==null){
           return new ApiMode("你要买的商品没有找到哦");
        }
        //添加购物车列表
        if (!carService.addCar(user.getUserId(),productId,productNum)) {
            return new ApiMode("添加购物车失败");
        }
        Integer carCount = carService.findCarCount(user.getUserId());
        Map<String,Object> map = new HashMap<>();
        map.put("carCount",carCount);
        //返回添加结果
        ApiMode apiMode = new ApiMode();
        apiMode.setDate(map);
        return apiMode;
    }

    /**
     * 显示用户的购物车数据
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/showUserCar")
    public ApiMode showUserCar(HttpServletRequest request){
        User user =(User) request.getSession().getAttribute("user");
        if (user==null){
            return new ApiMode("用户必须登录才能查看购物车");
        }
        //进行数据库该用户的购物车查询
        List<Car> carList = carService.findCarByUserId(user.getUserId());
        if (carList.size()==0){
            return new ApiMode("当前购物车内没有任何商品");
        }
        List<CarVo> carVoList = new ArrayList<>();
        CarVo carVo;
        Product product=null;
        //遍历查询商品信息
        for (Car car : carList){
            product= productService.findProductById(car.getPid(),0);//0无任何作用 补充位置
            if (product!=null){
                carVo = new CarVo();
                BeanUtils.copyProperties(product, carVo);
                carVo.setProductNum(car.getPNum());
                carVo.setProductPriceCount(product.getProductPrice().multiply(new BigDecimal(car.getPNum())));//设置商品总价
                carVo.setId(car.getId());
                carVoList.add(carVo);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("carVoList",carVoList);
        map.put("user",user);
        ApiMode apiMode = new ApiMode();
        apiMode.setDate(map);
        return apiMode;
    }

    /**
     * 商品的移除操作
     * @param productId
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/cancelCarProduct")
    public ApiMode cancelCarProduct(@RequestParam(value = "productId") int productId,
                                    HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(StringUtils.isBlank(String.valueOf(productId))||user==null){
            return new ApiMode("当前用户未登录，或者删除的商品编号不能为空");
        }
        if (!carService.clearCar(productId,user.getUserId())) {
            return new ApiMode("商品移除失败啦");
        }
        return new ApiMode();
    }

    @ResponseBody
    @PostMapping("/findCarCount")
    public ApiMode findCarCount(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return new ApiMode("用户在登录后才能查看购物车数据");
        }
        Integer carCount = carService.findCarCount(user.getUserId());
        Map<String,Object> map = new HashMap<>();
        map.put("carCount",carCount);
        ApiMode apiMode = new ApiMode();
        apiMode.setDate(map);
        return apiMode;
    }
}
