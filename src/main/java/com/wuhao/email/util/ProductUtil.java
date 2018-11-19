package com.wuhao.email.util;

import com.wuhao.email.dto.ProductDto;
import org.apache.commons.lang3.StringUtils;

public class ProductUtil {

    public static String getSerchUrl(ProductDto productDto){
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        if(StringUtils.isBlank(productDto.getKeyName())){
            sb.append("keyNam=");
        }else{
            sb.append("keyNam="+productDto.getKeyName());
        }

        return sb.toString();
    }
}
