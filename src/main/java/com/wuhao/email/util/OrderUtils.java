package com.wuhao.email.util;

import java.util.Date;

public class OrderUtils {

    public static String InitOrderId(){
        Date date = new Date();
        return String.valueOf(date.getTime()/100);
    }

}
