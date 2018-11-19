package com.wuhao.email.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.Product;
import com.wuhao.email.dto.ProductDto;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

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

    IPage<Product> listAllProduct(int page, ProductDto productDto);

    Workbook downLodeExcel(int type, int current,ProductDto productDto);

    boolean checkProductIsEntiy(ProductDto productDto);

    //前端

    List<Product> listAllProduct();

    Product findProductById(int productId,int productNum);

}
