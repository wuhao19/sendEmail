package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Product;
import com.wuhao.email.dto.ProductDto;
import com.wuhao.email.mapper.ProductMapper;
import com.wuhao.email.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public static final String POSITIVE_SEQUENCE = "0";//正序
    public static final String REVERSE_ORDER = "1";//倒序

    @Value("${myConfig.listProductSize}")
    private int LIST_PRODUCT_SIZE;

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
    public IPage<Product> listAllProduct(int current, ProductDto productDto) {
        IPage<Product> page = new Page<>(current,LIST_PRODUCT_SIZE);// 可以设置为配置文件
        IPage<Product> productIPage;
        //设置过滤条件
         if(!checkProductIsEntiy(productDto)){
             //直接进行筛选
             productIPage = productMapper.selectPage(page,new QueryWrapper<>());
         }else {
             productIPage = productMapper.selectPage(page,new QueryWrapper<Product>().
                     like(!StringUtils.isBlank(productDto.getKeyName()),"product_name",productDto.getKeyName()).
//                     like(!StringUtils.isBlank(productDto.getKeyName()),"product_description",productDto.getKeyName())
//                     orderBy(StringUtils.isBlank(productDto.getProductStockSort()),!productDto.getProductStockSort().equals(POSITIVE_SEQUENCE),"") //TODO 商品的库存正序倒序排列
                     eq(!StringUtils.isBlank(String.valueOf(productDto.getCategoryType())),"category_type",productDto.getCategoryType()).
                     eq(!StringUtils.isBlank(String.valueOf(productDto.getProductStatus())),"product_status",productDto.getProductStatus())
//                     between(!StringUtils.isBlank(String.valueOf(productDto.getStarDate())) && !StringUtils.isBlank(String.valueOf(productDto.getEndDate())),"update_time", Date.valueOf(String.valueOf(productDto.getStarDate())),Date.valueOf(String.valueOf(productDto.getEndDate()))
             );

         }
        if (productIPage==null) return null;
        return productIPage;
    }

    /**
     * 判断ProductDto是否为空
     * @param productDto
     * @return
     */
    @Override
    public boolean checkProductIsEntiy(ProductDto productDto){
        if (productDto==null){
            return false;
        }
        if (StringUtils.isBlank(productDto.getKeyName())){
            if (StringUtils.isBlank(productDto.getProductStockSort())){
                if (StringUtils.isBlank(productDto.getCategoryType())){
                    if (StringUtils.isBlank(productDto.getProductStatus())){
                        if (StringUtils.isBlank(productDto.getStarDate())){
                            if (StringUtils.isBlank(productDto.getEndDate())){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
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
            IPage<Product> productIPage = listAllProduct(current,new ProductDto());
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
