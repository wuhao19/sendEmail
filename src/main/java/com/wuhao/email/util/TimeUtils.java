package com.wuhao.email.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    /**
     * 获取邮件的验证有效时间
     * @return
     */
   public static Date getEmailVerifyExitTime(){
       Date date = new Date();
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(date);
       calendar.add(Calendar.DAY_OF_MONTH, +1);//+1今天的时间加一天
       date = calendar.getTime();
       return date;
   }

    /**
     * 获取当前的时间
     * @return
     */
   public static String getNowTimeString(){
       Date date = new Date();
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
       return simpleDateFormat.format(date);

   }
}
