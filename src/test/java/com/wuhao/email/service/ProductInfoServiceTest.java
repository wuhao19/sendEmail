package com.wuhao.email.service;

import com.wuhao.email.model.ProductInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceTest {
    @Autowired
    private ProductInfoService productInfoService;
    @Test
    public void addProductInfo() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("小米笔记本");
        productInfo.setProductDescription("15.6英寸屏幕，i20处理器，办公更高效");
        productInfo.setProductDetails("15.6英寸屏幕，i20处理器，办公更高效15.6英寸屏幕，i20处理器，办公更高效15.6英寸屏幕，i20处理器，办公更高效15.6英寸屏幕，i20处理器，办公更高效");
        productInfo.setProductPrice(new BigDecimal(9999));
        productInfo.setCategoryType(1);
        productInfo.setCommentaryGroup(1);
        productInfo.setIconGroup(1);
        productInfo.setProductStatus(1);
        productInfo.setProductStock(99);
        productInfo.setFounder("1");
        int result = productInfoService.addProductInfo(productInfo);
        System.out.println(result);
    }
}