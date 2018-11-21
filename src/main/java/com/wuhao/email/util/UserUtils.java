package com.wuhao.email.util;

import com.wuhao.email.domain.LoginMode;
import org.apache.commons.lang3.StringUtils;

public class UserUtils {
    public static final int MAX_LENGTH=32;
    public static final int MIN_LENGTH=1;
    public static final String EX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String EX_PHONE = "^[1][34578]\\d{9}$";
    /**
     * 判断登录信息是否合法
     * @param userName
     * @param password
     * @return
     */
    public static boolean checkScanInfo(String userName, String password) {
        if (StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
            return false;
        }
        if (userName.length()>MAX_LENGTH||password.length()>MAX_LENGTH){
            return false;
        }
        if (userName.length()<=MIN_LENGTH||password.length()<MIN_LENGTH){
            return false;
        }
        return true;
    }

    /**
     * 判断登陆的方式
     *
     * @param userName 登陆的用户账户
     * @return LoginMode
     */
    public static LoginMode loginType(String userName) {
        LoginMode mode = null;
        if (userName.matches(EX_EMAIL)) {//email登录
            mode = new LoginMode(LoginMode.LOGIN_TYPE_EMAIL);//1
        } else if (userName.matches(EX_PHONE)) {//手机号码登录
            mode = new LoginMode(LoginMode.LOGIN_TYPE_PHONE);//2
        } else {//用户名登录
            mode = new LoginMode(LoginMode.LOGIN_TYPE_USERNAME);//3
        }
        return mode;
    }
}
