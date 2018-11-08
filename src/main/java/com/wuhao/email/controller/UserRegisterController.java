package com.wuhao.email.controller;

import com.wuhao.email.domain.EmailMessage;
import com.wuhao.email.domain.LoginMode;
import com.wuhao.email.domain.RegisterMode;
import com.wuhao.email.domain.User;
import com.wuhao.email.service.EmailService;
import com.wuhao.email.service.UserService;
import com.wuhao.email.util.RegisterAssUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/register")
public class UserRegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String register(){
//        return new ModelAndView("register/register");
        return "register/register";
    }

    /**
     * 用户注册
     * @param user
     * @param verifyCode
     * @param model
     * @param request
     * @return
     */
    @PostMapping("/doRegister")
    public String doRegister(@Valid User user,
                             @RequestParam("verifyCode") String verifyCode,
                             Model model,
                             HttpServletRequest request
                           ){

        //验证传值的真实性以及手机验证
        RegisterMode registerMode = RegisterAssUtil.verifyRegisterInfo(user, verifyCode);
        if (registerMode==null||registerMode.getCode()!=0){
            model.addAttribute("message",registerMode.getMessage());
            model.addAttribute(user);
            return "register/register";
        }
        //调用service保存数据库
        User registerUser = userService.register(user);
        HttpSession session = request.getSession();
        //返回用户注册成功后的页面
        session.setAttribute("user",registerUser);
        model.addAttribute("emailVerify",registerUser.getUserEmailVerify());
        model.addAttribute("phoneVerify",registerUser.getUserPhoneVerify());
        return "index";
    }

    @ResponseBody
    @PostMapping("/sendEmail")
    public LoginMode  sendEmailVerifyLink(HttpServletRequest request){
        HttpSession session = request.getSession();
        User  user =(User) session.getAttribute("user");
        System.out.println("=========================>"+user.getUserName());
       return  new LoginMode();
    }


    /**
     * 用主页进行邮箱验证
     * @param userName
     * @param userEmail
     * @return
     */
    @GetMapping("/verifyEmail/{userName},{userEmail}")
    public String verifyEmail(@PathVariable("userName")String userName,
                              @PathVariable("userEmail")String userEmail){
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setForm("928707094@qq.com");
        emailMessage.setTo(userEmail);
        emailMessage.setSubject("邮箱认证");
        emailMessage.setText("这是一封验证邮件," +
                "恭喜您注册成功，请点击下面的连接进行激活账户:" +
                "http://127.0.0.1:8080/register/doVerifyEmail?userName="+userName+"&userEmail="+userEmail);
        emailService.sendHtmlEmail(emailMessage);
        return "index";
    }

    /**
     * 用户邮箱验证链接进行核实
     * @param userName
     * @param userEmail
     * @return
     */
    @GetMapping("/doVerifyEmail/{userName},{userEmail}")
    public ModelAndView doVerifyEmail(@PathVariable("userName") String userName, @PathVariable String userEmail,
                                      Map<String,Object> map,
                                      HttpSession session
                                      ){
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(userEmail)){
            map.put("message","用户或者邮箱不能为空");
            return new ModelAndView("common/error",map);
        }
        //调动UserService 查询是否用户
        User user=userService.findUserByEmail(userEmail,session);
        if(null==user || user.getUserName()!=userName ){
            map.put("message","验证失败！该用户不存在。");
            return new ModelAndView("common/error",map);
        }
        user.setUserEmailVerify(0);
        userService.updateUser(user,session);
        return new ModelAndView("index");
    }
}
