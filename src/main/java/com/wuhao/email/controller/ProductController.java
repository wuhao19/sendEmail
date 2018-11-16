package com.wuhao.email.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuhao.email.domain.Product;
import com.wuhao.email.domain.User;
import com.wuhao.email.dto.ProductDto;
import com.wuhao.email.excptionHandler.MyException;
import com.wuhao.email.service.IProductService;
import com.wuhao.email.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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





    @Autowired
    private IProductService productService;

    @GetMapping("/addProduct")
    public String addProduct(Model model){
        Product product = new Product();
        model.addAttribute(product);
        return "product/addProduct";
    }

    @GetMapping("/Downloads")
    public void downLodeExcel(@RequestParam(value = "type",defaultValue = "0")int type,
                              @RequestParam(value = "page",defaultValue = "1")int page,
                              HttpServletResponse response){
        Workbook workbook;
        // 判断导出方式
        if (type==1){
          //执行导出当前页面
           workbook= productService.downLodeExcel(type,page);
            if (!initExcelFile(response,workbook)){
                throw new MyException(5,"【下载文件】：进行下载文集的初始化失败");
            }
        }else if (type==2){
            workbook= productService.downLodeExcel(type,page);
            if (!initExcelFile(response,workbook)){
                throw new MyException(5,"【下载文件】：进行下载文集的初始化失败");
            }
        }else {
            throw new MyException(5,"【下载文件】：目标文件需要下载的部分出现错误");
        }
    }

    /**
     * 初始化Excel下载文件
     * @param response
     * @param workbook
     * @return
     */
    private boolean initExcelFile(HttpServletResponse response,Workbook workbook){
        if (response==null || workbook == null){
            return false;
        }
        String fileName = TimeUtils.getNowTimeString()+".xls";
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            workbook.write(response.getOutputStream());
            //response.flushBuffer();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取商品的列表
     * @param page
     * @param model
     * @return
     */
    @RequestMapping("/listProduct")
    public String listProduct(@RequestParam(value = "page",defaultValue = "1") String page,
                              @Valid ProductDto productDto,
                              HttpServletRequest request,
                              Model model){
        HttpSession session = request.getSession();
        ProductDto productDtoSession = (ProductDto) session.getAttribute("productDto");
        if (!productService.checkProductIsEntiy(productDtoSession)){
            session.setAttribute("productDto",productDto);
        }else {
            //session 已经设置
           if (!productDtoSession.equals(productDto)){
               if (!productService.checkProductIsEntiy(productDto)){
                   productDto = (ProductDto) session.getAttribute("productDto");
               }else {
                   session.setAttribute("productDto",productDto);//查找值已经改变
               }
           }
        }

        int current = initPage(page);//初始化查询页面
        // 获取查询的商品
        IPage<Product> productIPage = productService.listAllProduct(current,productDto);
        if (productIPage==null){
            throw new MyException(1,"商品库没有任何商品");
        }
        long pageCount = productIPage.getPages();//返回当前查询类型的最大页数用于前端分页
        List<Product> productList = productIPage.getRecords();//返回当前页面展示的类容
        long total = productIPage.getTotal();//返回查询到的总记录数
        model.addAttribute("pageCount",pageCount);//总页数
        model.addAttribute("productList",productList);
        model.addAttribute("total",total);//总商品数量
        model.addAttribute("current",current);//当前页
        model.addAttribute("productDto",productDto);
        return "product/listProduct";
    }

    /**
     * 对用户请求的商品列表页进行初始化
     * @param page
     * @return
     */
    private int initPage(String page){
        if (StringUtils.isBlank(page)){
            return 1;//默认查询第一页
        }else {
             int current = Integer.valueOf(page);
             if (current<0){
                 return 1;
             }else {
                 return current;
             }
        }
    }
    /**
     * 用ecxel批量上传文件
     * @param multipartFile
     * @param session
     * @return
     */
    @PostMapping("/doAddProduct/excel")
    public String doAddProductFromExcel(@RequestParam("multipartFile") MultipartFile multipartFile,HttpServletRequest request){
        //获取上传的用户信息
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        // 判断文件是否合法
        if (!checkProductExcelFile(multipartFile)) {
            throw new MyException(2,"你上传的文件不合法");
        }
        // 进行文件解析成为一个商品list
        try {
            List<Product> productList = analysisExcelToProduct(multipartFile);
            if (productList.size()==0){
                throw new MyException(2,"商品文件解析完成后，未发现商品");
            }
            // 调用server 进行数据存储
            if (!saveProductList(productList,user)) {
                throw new MyException(2,"商品在保存到数据库时失败啦");
            }
        }catch (Exception e){
            throw new MyException(2,"用ecxel批量上传文件时失败");
        }
        //保存成功 返回商品的list 页面
        return "product/listProduct";
    }

    /**
     * 批量进行商品保存到数据库
     * @param productList
     * @return
     */
    private boolean saveProductList(List<Product> productList,User user){
        if(productList.size()==0){
            throw new MyException(3,"批量进行商品保存到数据库时，商品列表为空");
        }
        for (Product product :productList){
            product.setUpdateBy(user.getUserId());
            product.setCrateBy(user.getUserId());
            if(!productService.addProductFrom(product)){
                throw new MyException(3,"批量进行商品保存到数据库时，商品保存失败");
            }
        }
        return true;
    }
    /**
     * 把Excel文件解析为商品列表
     * @param file
     * @return
     */
    private List<Product> analysisExcelToProduct(MultipartFile file) throws Exception{
        if (file==null){
            return null;
        }
        List<Product> productList = new ArrayList<>();
        InputStream is = file.getInputStream();
        Workbook workbook = null;
        if(file.getOriginalFilename().endsWith(".xls")){//判断文件是否是2003版本
            workbook = new HSSFWorkbook(is);
        }else {
            workbook = new XSSFWorkbook(is);
        }
        Sheet sheetAt = workbook.getSheetAt(0);//
        if (sheetAt==null){
            return null;
        }
        Product product;
        for (int r=1;r<=sheetAt.getLastRowNum();r++){
            Row row = sheetAt.getRow(r);
            if (row==null){
                break;//如果取出来 的值是空的，直接跳出当前循环
            }
            //取值
            product = readProductFormRow(row);
            if (product==null){
                log.error("从Row中取出一个商品失败");
            }else {
                productList.add(product);
            }
        }
        return productList;
    }

    /**
     * 把excel文件的每一行读取存为product的属性
     * @param row
     * @return
     */
    private Product readProductFormRow(Row row){
        if (row == null){
            return null;
        }
        row.getCell(0).setCellType(HSSFCell.CELL_TYPE_STRING);//将数字做字符串处理
        String productName = row.getCell(0).getStringCellValue();
        if (StringUtils.isBlank(productName)){
            log.error("商品名称的列不能为空");
            throw new MyException(6,"【上传商品文件失败】：商品名称的列不能为空");
        }
        String productDescription = row.getCell(1).getStringCellValue();
        if (StringUtils.isBlank(productDescription)){
            log.error("商品的描述不能为空");
            throw new MyException(6,"【上传商品文件失败】：商品的描述不能为空");
        }
        String productDetails = row.getCell(2).getStringCellValue();
        if (StringUtils.isBlank(productDetails)){
            log.error("商品的详细描述不能为空");
            throw new MyException(6,"【上传商品文件失败】：商品的详细描述不能为空");
        }

        double productPriceD = row.getCell(3).getNumericCellValue();
        if (productPriceD==0.0){
            log.error("商品的单价不能为空");
            throw new MyException(6,"【上传商品文件失败】：商品的单价不能为空");
        }
        BigDecimal porductPrice = new BigDecimal(productPriceD);

        row.getCell(4).setCellType(HSSFCell.CELL_TYPE_STRING);//将数字做字符串处理
        String categoryTypeString = row.getCell(4).getStringCellValue();
        if (StringUtils.isBlank(categoryTypeString)){
            log.error("商品的类型不能为空");
            throw new MyException(6,"【上传商品文件失败】：商品的类型不能为空");
        }
        int categoryType = Integer.parseInt(categoryTypeString);

        row.getCell(5).setCellType(HSSFCell.CELL_TYPE_STRING);//将数字做字符串处
        String productStatusString = row.getCell(5).getStringCellValue();//商品的状态 1为下架  0位上架
        if (StringUtils.isBlank(productStatusString)){
            log.error("商品的状态不能为空");
            throw new MyException(6,"【上传商品文件失败】：商品的状态不能为空");
        }
        int productStatus = Integer.parseInt(productStatusString);
        Product product = new Product();
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductDetails(productDetails);
        product.setCategoryType(categoryType);
        product.setProductPrice(porductPrice);
        product.setProductStatus(productStatus);
        product.setCrateBy(1);
        product.setUpdateBy(1);
        return product;
    }

    /**
     * 判断用户上传的文件 是否是一个Excel文件
     * @param file
     * @return
     */
    private boolean checkProductExcelFile(MultipartFile file){
        if (file.isEmpty()){
            return false;
        }
        // 判断传入的文件类型
        String  fileName= file.getOriginalFilename();
        if (!fileName.endsWith(".xls")&&!fileName.endsWith(".xlsx")&&!fileName.endsWith(".xlsm")){
            log.error("传入的文件不是一个Excel文件");
            throw new MyException(6,"【上传商品文件失败】：传入的文件不是一个Excel文件");
        }
        return true;
    }

    /**
     * 表单提交添加商品
     * @param product
     * @return
     */
    @PostMapping("/doAddProduct")
    public String doAddProduct(@Valid Product product,
                               HttpServletRequest request,
                               Model model){
        HttpSession session = request.getSession();
        // 检查用户添加的商品信息是否合法
        if(!checkProduct(product)){
            //返回商品信息不合法
            model.addAttribute("message","商品添加失败，原因：商品信息录入不合法");
            session.setAttribute("product",product);
            return "product/addProduct";
        }
        //获取操作用户的信息
        User user = (User) session.getAttribute("user");
        // 进行商品的添加操作
        if (!addProduct(product,user)) {
            //返回商品信息不合法
            model.addAttribute("message","商品添加失败，原因：商品录入数据库失败");
            session.setAttribute("product",product);
            return "product/addProduct";
        }
        // 添加成功 返回商品列表页面
        model.addAttribute("message","商品添加成功");
        return "redirect:/product/listProduct";
    }

    /**
     * 进行表单商品的添加操作
     * @param product
     * @return
     */
    private boolean addProduct(Product product,User user){
        if (product==null||user==null){
            throw new MyException(2,"上传的商品或者，当前操作的用户信息不合法");
        }
        product.setCrateBy(user.getUserId());
        product.setUpdateBy(user.getUserId());
        if (!productService.addProductFrom(product)) {
            throw new MyException(2,"[添加商品]，添加商品时对数据库操作失败");
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
