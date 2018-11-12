package com.wuhao.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.Product;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
public interface IProductService extends IService<Product> {

    boolean addProductFrom(Product product);

}
