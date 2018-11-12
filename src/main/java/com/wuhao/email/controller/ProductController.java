package com.wuhao.email.controller;


import com.wuhao.email.domain.Product;
import com.wuhao.email.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {
    public static final int ADD_PRODUCT_TYPE_FORM=1;//表单提交
    public static final int ADD_PRODUCT_TYPE_EXCEL=2;//单个商品添加

    @Autowired
    private IProductService productService;

    @GetMapping("/addProduct")
    public String addProduct(Model model){
        Product product = new Product();
        model.addAttribute(product);
        return "product/addProduct";
    }

    /**
     * 表单提交添加商品
     * @param product
     * @return
     */
    @PostMapping("/doAddProduct")
    public String doAddProduct(@Valid Product product,
                               HttpServletRequest request){
        HttpSession session = request.getSession();
        // 检查用户添加的商品信息是否合法
        if(!checkProduct(product)){
            //返回商品信息不合法
            session.setAttribute("message","商品添加失败，原因：商品信息录入不合法");
            session.setAttribute("product",product);
            return "product/addProduct";
        }
        // 进行商品的添加操作
        if (!addProduct(product)) {
            //返回商品信息不合法
            session.setAttribute("message","商品添加失败，原因：商品录入数据库失败");
            session.setAttribute("product",product);
            return "product/addProduct";
        }
        // 添加成功 返回商品列表页面
        session.setAttribute("message","商品添加成功");
        return "product/listProduct";
    }

    /**
     * 进行表单商品的添加操作
     * @param product
     * @return
     */
    private boolean addProduct(Product product){
        if (product==null){
            return false;
        }
        if (!productService.addProductFrom(product)) {
            return false;
        }
        return true;
    }
    /**
     * 检查商品的合法字段的合法性
     * @param product
     * @return
     */
    private boolean checkProduct(Product product){
        if (product==null){
            log.error("添加的商品不能为空");
            return false;
        }
        if (!checkProductName(product)) {
            return false;
        }
        if (!checkProductDescription(product)) {
            return false;
        }
        if (!checkProductDetails(product)) {
            return false;
        }
        if (!checkProductPrice(product)) {
            return false;
        }
        if (!checkCategoryType(product)) {
            return false;
        }
        return true;
    }

    /**
     * 检查商品的名称合法性
     * @param product
     * @return
     */
    private boolean checkProductName(Product product){
        if (product==null){
            log.error("添加的商品不能为空");
            return false;
        }
        if (StringUtils.isBlank(product.getProductName())){
            log.error("添加的商品名称不合法");
            return false;
        }
        return true;
    }
    /**
     * 检查商品的描述合法性
     * @param product
     * @return
     */
    private boolean checkProductDescription(Product product){
        if (product==null){
            log.error("添加的商品不能为空");
            return false;
        }
        if (StringUtils.isBlank(product.getProductDescription())){
            log.error("添加的商品描述不合法");
            return false;
        }
        return true;
    }
    /**
     * 检查商品的详细描述合法性
     * @param product
     * @return
     */
    private boolean checkProductDetails(Product product){
        if (product==null){
            log.error("添加的商品不能为空");
            return false;
        }
        if (StringUtils.isBlank(product.getProductDetails())){
            log.error("添加的商品详细描述不合法");
            return false;
        }
        return true;
    }
    /**
     * 检查商品的价格
     * @param product
     * @return
     */
    private boolean checkProductPrice(Product product){
        if (product==null){
            log.error("添加的商品不能为空");
            return false;
        }
        if (product.getProductPrice()==null) {
            log.error("添加的商品价格不能为空");
            return false;
        }
        BigDecimal min = new BigDecimal(0.00);
        if (product.getProductPrice().compareTo(min)==-1){//小于0
            log.error("添加的商品的价格不合法");
            return false;
        }
        return true;
    }
    /**
     * 检查商品的类型
     * @param product
     * @return
     */
    private boolean checkCategoryType(Product product){
        if (product==null){
            log.error("添加的商品不能为空");
            return false;
        }
        if (product.getCategoryType().equals("")){
            log.error("添加的商品类型不合法");
            return false;
        }
        return true;
    }

}
