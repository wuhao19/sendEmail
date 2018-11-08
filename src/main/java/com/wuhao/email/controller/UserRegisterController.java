package com.wuhao.email.controller;

import com.wuhao.email.domain.EmailMessage;
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

    EmailMessage emailMessage=null;

    String verifySmsCode=null;

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
        RegisterMode registerMode = RegisterAssUtil.verifyRegisterInfo(user,verifyCode,verifySmsCode);
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

    /**
     * 注册时用户获取验证码
     * @param phone
     * @param
     */
    @ResponseBody
    @PostMapping("/sendPhone")
    public RegisterMode sendPhone(@RequestParam("phone") String phone){
        //生产6位验证码
        verifySmsCode = RegisterAssUtil.getVerifySmsCode();
        String verifyCode = RegisterAssUtil.getVerifyCode(phone, verifySmsCode);
        if (verifyCode==null){
            return new RegisterMode("获取验证码失败");
        }
        return new RegisterMode();
    }

    /**
     * 用主页进行邮箱验证
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/sendEmail")
    public void   sendEmailVerifyLink(HttpServletRequest request){
        HttpSession session = request.getSession();
        User  user =(User) session.getAttribute("user");
        emailMessage = new EmailMessage();
        emailMessage.setForm("928707094@qq.com");
        emailMessage.setTo(user.getUserEmail());
        emailMessage.setSubject("邮箱认证");
        String emailText ="这是一封验证邮件,恭喜您注册成功，请点击下面的连接进行激活账户: http://127.0.0.1:8080/register/doVerifyEmail?userName="+user.getUserName()+"&userEmail="+user.getUserEmail();
        System.out.println(emailText);
        emailMessage.setText(emailText);
        emailService.sendHtmlEmail(emailMessage);
    }


    /**
     * 用户邮箱验证链接进行核实
     * @param userName
     * @param userEmail
     * @return
     */
    @GetMapping("/doVerifyEmail")
    public ModelAndView doVerifyEmail(@RequestParam("userName") String userName,@RequestParam("userEmail") String userEmail,
                                      Map<String,Object> map
                                      ){
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(userEmail)){
            map.put("message","用户或者邮箱不能为空");
            return new ModelAndView("common/error",map);
        }
        //调动UserService 查询是否用户
        User user=userService.findUserByEmail(userEmail);
        if(null==user || !user.getUserName().equals(userName) ){
            map.put("message","验证失败！该用户不存在。");
            return new ModelAndView("common/error",map);
        }
        user.setUserEmailVerify(0);
        userService.updateUser(user);
        map.put("message","邮箱验证成功");
        return new ModelAndView("common/success",map);
    }
}
