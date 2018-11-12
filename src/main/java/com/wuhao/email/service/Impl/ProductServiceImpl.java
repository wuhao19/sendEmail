package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Product;
import com.wuhao.email.mapper.ProductMapper;
import com.wuhao.email.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    /**
     * 进行表单商品的添加操作
     * @param product
     * @return
     */
    @Override
    public boolean addProductFrom(Product product) {
        if (product==null){
            return false;
        }
        int result = productMapper.insert(product);
        if (result!=1){
            return false;
        }
        return true;
    }

}
