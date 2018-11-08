package com.wuhao.email.util;

import com.wuhao.email.domain.RegisterMode;
import com.wuhao.email.domain.User;
import com.wuhao.email.restDemo.YUNSMS;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class RegisterAssUtil {

    public static final String EX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String EX_PHONE = "^[1][34578]\\d{9}$";
    public static final int MAX_VALUE=128;

    /**
     * 注册传值的格式验证
     * @param user
     * @param verifyCode
     * @return
     */
    public static RegisterMode verifyRegisterInfo(User user,String verifyCode) {
        RegisterMode registerMode = null;
        if(StringUtils.isEmpty(user.getUserName()) || user.getUserName().length()>=MAX_VALUE){
            return new RegisterMode("用户名不能为空或者输入格式有误");
        }else if(StringUtils.isEmpty(user.getUserPassword())||user.getUserPassword().length()>=MAX_VALUE){
            return new RegisterMode("用户的密码不能为空或者输入的密码格式有误");
        }else if (StringUtils.isEmpty(user.getUserEmail()) || user.getUserEmail().length()>=MAX_VALUE){
            return new RegisterMode("用户的Email不能为空或者输入的格式有误");
        }else if((StringUtils.isEmpty(user.getUserPhone()) || !user.getUserPhone().matches(EX_PHONE)) || user.getUserPhone().length()>=MAX_VALUE){
            return new RegisterMode("用户的手机号不能为空或者手机号输入错误");
        }else if(StringUtils.isEmpty(verifyCode) || verifyCode.length()!=6){
            return new RegisterMode("用户的手机验证码不能为空或者格式不正确");
        }else {
//            //进行手机验证码验证
//            String resultVerifyCode = getVerifyCode(user.getUserPhone());
//            if (StringUtils.isBlank(resultVerifyCode)|| !resultVerifyCode.equals(verifyCode)){
//                return new RegisterMode("你输入的验证码不对，请重新输入");
//            }
            return registerMode = new RegisterMode();
        }
    }

    public static String getVerifySmsCode(){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i<6;i++){
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 发送验证码
     * @param userPhone
     */
    public static String getVerifyCode(String userPhone){
        String verifySmsCode = RegisterAssUtil.getVerifySmsCode();
        //获取短信发送的对象
        YUNSMS yunsms = new YUNSMS();
        yunsms.sendSms(userPhone,verifySmsCode);
        return verifySmsCode;
    }

}
