package com.wuhao.email.controller;

import com.wuhao.email.domain.LoginMode;
import com.wuhao.email.domain.User;
import com.wuhao.email.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
@Slf4j
public class UserLoginController {
    public static final int MAX_LANG = 128;
    public static final String EX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String EX_PHONE = "^[1][34578]\\d{9}$";

    @Autowired
    private IUserService userService;

    /**
     * 初始化登录页面
     *
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "login/login";
    }

    /**
     * 用户进行登录操作
     *
     * @param userName     用户名or电话or邮箱
     * @param userPassword 密码
     * @param model
     * @param request
     * @return
     */
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("userName") String userName,
                          @RequestParam("userPassword") String userPassword,
                          Model model,
                          HttpServletRequest request
    ) {
        //检查传值的 userName 和 password 是否合法
        if (!checkScanInfo(userName, userPassword)) {
            //用户输入信息不合法
        }
        //判断用户的登录方式
        LoginMode loginMode = loginType(userName);
        //进行登录操作
        User user = doLoginByMode(loginMode, userName, userPassword);
        if (user==null) {
            model.addAttribute("message", "账户或者密码错误");
            return "login/login";
        }
        //设置session
        HttpSession session = request.getSession();
        session.setAttribute("user",user);
        //登录成功
        return "index";
    }
    /**
     * 进行登录操作
     * @param loginMode
     * @return
     */
    private User doLoginByMode(LoginMode loginMode, String userName, String password) {
        if (loginMode == null) {
            return null;
        }
        User user = null;
        switch (loginMode.getLoginType()) {
            case 1:
                //邮箱登录
                user = userService.loginByEmail(userName,password);
                if (user==null) {
                    return null;
                }
                break;
            case 2:
                //电话登录
                user = userService.loginByPhone(userName, password);
                if (user==null) {
                    return null;
                }
                break;
            case 3:
                //用户名登录
                user=userService.loginByUserName(userName, password);
                if (user==null) {
                    return null;
                }
                break;
        }
        return user;
    }

    /**
     * 判断登陆的方式
     *
     * @param userName 登陆的用户账户
     * @return LoginMode
     */
    private LoginMode loginType(String userName) {
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

    /**
     * @param userName
     * @param password
     * @return
     */
    private boolean checkScanInfo(String userName, String password) {
        if (!checkUserName(userName) || !checkPassword(password)) {
            return false;
        }
        return true;
    }

    /**
     * 检查用户输入的用户名
     *
     * @param userName
     * @return
     */
    private boolean checkUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return false;
        }
        if (userName.length() > MAX_LANG) {
            return false;
        }
        return true;
    }

    /**
     * 检查用户输入的密码
     *
     * @param password
     * @return
     */
    private boolean checkPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        if (password.length() > MAX_LANG) {
            return false;
        }
        return true;
    }

}
