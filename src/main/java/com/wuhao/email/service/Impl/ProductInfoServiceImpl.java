package com.wuhao.email.service.Impl;

import com.wuhao.email.dao.ProductInfoDao;
import com.wuhao.email.model.ProductInfo;
import com.wuhao.email.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductInfoDao productInfoDao;
    @Override
    public int addProductInfo(ProductInfo productInfo) {
        return productInfoDao.addProductInfo(productInfo);
    }
}
