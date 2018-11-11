package com.wuhao.email.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
@RestController
@RequestMapping("/model/stock")
public class ProductStockController {
    @PostMapping("/test")
    public String test(){
       return "index";
    }
}
