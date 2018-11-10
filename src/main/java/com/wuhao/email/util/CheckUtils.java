package com.wuhao.email.util;

import org.apache.commons.lang3.StringUtils;

public class CheckUtils {
    public static boolean checkEmial(String email){
        if (StringUtils.isBlank(email)){
            return false;
        }

    }
}
