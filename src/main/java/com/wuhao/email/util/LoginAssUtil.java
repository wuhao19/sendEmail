package com.wuhao.email.util;


import com.wuhao.email.domain.LoginMode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class LoginAssUtil {

    public static final String EX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String EX_PHONE = "^[1][34578]\\d{9}$";

    /**
     * 判断登陆的信息是否合法，合法后检测登陆的方式
     *
     * @param userName     登陆的用户账户
     * @param userPassword 登陆的密码
     * @return LoginMode
     */
    public static LoginMode isLoginInfoNull(String userName, String userPassword) {
        LoginMode mode = null;
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(userPassword)) {
            log.error("[登录错误：]:用户名或密码未输入");
            return new LoginMode("用户名或密码未输入");
        }
        if (userName.length() < 5 || userName.length() > 24) {
            log.error("[登录错误]：用户名不合法");
            return new LoginMode("用户名不合法");
        }
        if (userName.matches(EX_EMAIL)) {//email登录
            mode = new LoginMode(LoginMode.LOGIN_TYPE_EMAIL);
        } else if (userName.matches(EX_PHONE)) {//手机号码登录
            mode = new LoginMode(LoginMode.LOGIN_TYPE_PHONE);
        } else {//用户名登录
            mode = new LoginMode(LoginMode.LOGIN_TYPE_ACCESS);
        }
        return mode;
    }

}
