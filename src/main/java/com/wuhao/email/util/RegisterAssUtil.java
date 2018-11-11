package com.wuhao.email.util;

import com.wuhao.email.YUNSMS.YUNSMS;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class RegisterAssUtil {

    /**
     * 生产6位数随机验证码
     * @return 随机6位验证码
     */
    public static String getVerifySmsCode(){
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0;i<6;i++){
            stringBuffer.append(random.nextInt(10));
        }
        return stringBuffer.toString();
    }

    /**
     * 发送手机验证码
     * @param userPhone 用户的手机号
     * @param systemPhoneVerifyCode 系统生成的6位验证码
     * @return null 短信发送失败
     */
    public static String getVerifyCode(String userPhone,String systemPhoneVerifyCode){
        //获取短信发送的对象
        YUNSMS yunsms = new YUNSMS();
        String result = yunsms.sendSms(userPhone, systemPhoneVerifyCode);
        if(StringUtils.isBlank(result)){
            return null;
        }
        return result;
    }
}
