package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Product;
import com.wuhao.email.mapper.ProductMapper;
import com.wuhao.email.service.IProductService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 展示商品页面
     * @param current
     * @return
     */
    @Override
    public IPage<Product> listAllProduct(int current) {
        IPage<Product> page = new Page<>(current,3);//TODO 可以设置为配置文件
        IPage<Product> productIPage = productMapper.selectPage(page,new QueryWrapper<>());
        if (productIPage==null) return null;
        return productIPage;
    }

    /**
     * 导出商品信息
     * @param type
     */
    @Override
    public Workbook downLodeExcel(int type,int current) {
        //根据类型获取 商品的列表
        List<Product> productList;
        if (type==1){//获取用户当前页的内容
            IPage<Product> productIPage = listAllProduct(current);
            productList = productIPage.getRecords();
        }else {
            productList = productMapper.selectList(new QueryWrapper<>());
        }
        //构建Excel文件
        Workbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = ((HSSFWorkbook) workbook).createSheet("商品信息表");

        String[] headers = { "商品名称", "商品的描述", "商品的详细描述", "商品的单价","商品的类型（数字）","商品的状态 1为下架  0位上架"};//设置excel头
        //设置表头
        HSSFRow row = sheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            HSSFCell cell =row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        //放入查询的商品表信息
        int rowNum = 1;
        for (Product product: productList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(product.getProductName());
            row1.createCell(1).setCellValue(product.getProductDescription());
            row1.createCell(2).setCellValue(product.getProductDetails());
            String productPrice = product.getProductPrice().toString();
            row1.createCell(3).setCellValue(productPrice);
            row1.createCell(4).setCellValue(product.getCategoryType());
            row1.createCell(5).setCellValue(product.getProductStatus());
            rowNum++;
        }
        return workbook;
    }
}
