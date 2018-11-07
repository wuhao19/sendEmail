package com.wuhao.email.controller;

import com.wuhao.email.Enum.EmailCode;
import com.wuhao.email.domain.LoginMode;
import com.wuhao.email.domain.User;
import com.wuhao.email.excption.MyExcption;
import com.wuhao.email.service.UserService;
import com.wuhao.email.util.LoginAssUtil;
import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/login")
public class UserLoginController {
    @Autowired
    private UserService userService;

    private  User userByUseAndPassword;

    @GetMapping("/index")
    public String index(){
        return "login/index";
    }

    @PostMapping("/dologin")
    public String dologin(@RequestParam("userName")String userName,
                          @RequestParam("userPassword")String userPassword,
                          Model model,
                          HttpServletRequest request
                          ){
        LoginMode loginMode = LoginAssUtil.isLoginInfoNull(userName, userPassword);
    if(loginMode.getCode() !=0){
        model.addAttribute("userName",userName);
        model.addAttribute("message",loginMode.getMessage());
        return "login/index";
    }else{
        //调用Service 登陆验证
      User user =  userService.login(userName,userPassword);
      if(user == null){
          model.addAttribute("userName",userName);
          model.addAttribute("message","账户和密码错误");
          return "login/index";
      }else{
          HttpSession session =request.getSession();
          session.setAttribute("user",user);
          return "form";
        }
    }
    }

}
