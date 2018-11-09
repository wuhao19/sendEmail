package com.wuhao.email.dao;

import com.wuhao.email.model.ProductInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductInfoDao {
    /**
     * 新增一个商品
     * @param productInfo
     * @return
     */
    int addProductInfo(ProductInfo productInfo);
}
