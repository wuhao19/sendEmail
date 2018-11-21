package com.wuhao.email.util;

import java.util.Date;

public class OrderUtils {

    public static String InitOrderId(){
        Date date = new Date();
        String orderId = String.valueOf(date.getTime() / 100);
        return orderId;
    }

}
