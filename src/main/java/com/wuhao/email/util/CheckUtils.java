package com.wuhao.email.util;

import org.apache.commons.lang3.StringUtils;

public class CheckUtils {
    public static final String EX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String EX_PHONE = "^[1][34578]\\d{9}$";
    public static final int MAX_VALUE = 128;

    /**
     * 检查email
     * @param email 邮件
     * @return
     */
    public static boolean checkEmial(String email){
        if (StringUtils.isBlank(email)){
            return false;
        }
        if (!email.matches(EX_EMAIL)){
            return false;
        }
        if(email.length()>MAX_VALUE){
            return false;
        }
        return true;
    }

    /**
     * 检查电话号码
     * @param phone 电话号码
     * @return
     */
    public static boolean checkPhone(String phone){
        if (StringUtils.isBlank(phone)){
            return false;
        }
        if (!phone.matches(EX_PHONE)){
            return false;
        }
        if (phone.length()>MAX_VALUE){
            return false;
        }
        return true;
    }

    /**
     * 检查字符串
     * @param s 任意字符串
     * @return
     */
    public static boolean checkUtils(String s){
        if (StringUtils.isBlank(s)){
            return false;
        }
        if (s.length()>MAX_VALUE){
            return false;
        }
        return true;
    }
    public static boolean checkUtils(String s1,String s2){
        if (StringUtils.isBlank(s1)||StringUtils.isBlank(s2)){
            return false;
        }
        if (s1.length()>MAX_VALUE||s2.length()>MAX_VALUE){
            return false;
        }
        return true;
    }
}
