package com.wuhao.email.controller;

import com.wuhao.email.domain.ApiMode;
import com.wuhao.email.domain.Product;
import com.wuhao.email.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/product")
public class UserProductController {
    @Autowired
    private IProductService productService;

    @ResponseBody
    @PostMapping("/listAllProduct")
    public ApiMode listAllProduct(){
        List<Product> productList = productService.listAllProduct();
        if (productList.size()==0){
            return new ApiMode("当前商城没有任何商品");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("productList",productList);
        ApiMode apiMode = new ApiMode();
        apiMode.setDate(map);
        return apiMode;
    }


}
