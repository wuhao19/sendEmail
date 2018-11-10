package com.wuhao.email.controller;

import com.wuhao.email.domain.RegisterMode;
import com.wuhao.email.domain.RegisterUserInfoMode;
import com.wuhao.email.service.EmailService;
import com.wuhao.email.service.UserService;
import com.wuhao.email.util.CheckUtils;
import com.wuhao.email.util.RegisterAssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/register")
public class UserRegisterController {

    private static final String PHONE_KEY = "PHONE_KEY";

    public static final int MAX_VALUE = 128;


    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String register(Model model) {
        RegisterUserInfoMode registerUserInfoMode = new RegisterUserInfoMode();
        model.addAttribute("user", registerUserInfoMode);
        return "register/register";
    }

    /**
     * 注册用户提交的信息页面
     *
     * @param userInfoMode 获取用户输入信息
     * @param model        返换用户的对象
     * @param request      request请求
     * @return
     */
    @RequestMapping("/doRegister")
    public String doRegister(@Valid RegisterUserInfoMode userInfoMode, Model model, HttpServletRequest request) {
        // 检查用户数据
        String checkRegisterUserInfo = checkUserInfoEnpty(userInfoMode);
        if (checkRegisterUserInfo != null) {
            setRegeditError(model, checkRegisterUserInfo, userInfoMode);
            return "register/register";
        }
        //获取手机验证码
        HttpSession session = request.getSession();
        String systemVerifyCode = (String) session.getAttribute(PHONE_KEY);

        //检查用户的验证码
        if (!checkPhoneVerifyCode(systemVerifyCode, userInfoMode.getPhoneCode())) {
            setRegeditError(model, "验证码输入错误", userInfoMode);
            return "register/register";
        }
        //验证成功
        // Service保存用户信息
        // 清除Session 手机验证码
        session.removeAttribute(PHONE_KEY);
        session.setAttribute("user", userInfoMode);
        //发送邮箱验证
        String emailVerifyCode = emailService.sendVerifyEmail(userInfoMode.getUserEmail());
        session.setAttribute("emailVerifyCode",emailVerifyCode);
        return "index";
    }
    @PostMapping("/sendEmail")
    public void sendEmail(HttpServletRequest request){
        HttpSession session = request.getSession();
        RegisterUserInfoMode userInfoMode =(RegisterUserInfoMode) session.getAttribute("user");
        String emailVerifyCode = emailService.sendVerifyEmail(userInfoMode.getUserEmail());
        session.setAttribute("emailVerifyCode",emailVerifyCode);
    }

    /**
     * 注册过程中出现错误封装返回的数据格式
     *
     * @param model 返换用户的对象
     * @param msg 返回页面的message信息
     * @param registerUserInfoMode 用户的model对象
     */
    private void setRegeditError(Model model, String msg, RegisterUserInfoMode registerUserInfoMode) {
        model.addAttribute("message", msg);
        model.addAttribute("user", registerUserInfoMode);
    }

    /**
     * 检查用户的信息是否合法
     * @param userInfoMode 用户的信息
     * @return boolean
     */
    private String checkUserInfoEnpty(RegisterUserInfoMode userInfoMode) {
        String message = null;

        if (userInfoMode == null) {
            return message = "用户不能为空或者输入格式有误";
        }
        if(!CheckUtils.checkUtils(userInfoMode.getUserName())){
            return message = "用户名不能为空或者输入格式有误";
        }
        if (!CheckUtils.checkUtils(userInfoMode.getUserPassword())) {
            return message = "用户的密码不能为空或者输入的密码格式有误";
        }
        if (!CheckUtils.checkEmial(userInfoMode.getUserEmail())) {
            return message = "用户的Email不能为空或者输入的格式有误";
        }
        if (!CheckUtils.checkPhone(userInfoMode.getUserPhone())) {
            return message = "用户的手机号不能为空或者手机号输入错误";
        }
        if (!CheckUtils.checkUtils(userInfoMode.getPhoneCode())) {
            return message = "用户输入的验证码格式不正确";
        }
        return null;
    }

    /**
     * 检查用户的手机验证码是否正确
     * @param systemPhoneVerifyCode 系统验证码
     * @param userPhoneVerifyCode   用户输入的验证码
     * @return 验证错误信息对象
     */
    private boolean checkPhoneVerifyCode(String systemPhoneVerifyCode, String userPhoneVerifyCode) {
        if (!CheckUtils.checkUtils(systemPhoneVerifyCode)|| !CheckUtils.checkUtils(userPhoneVerifyCode)) {
            return false;
        }
        if (!systemPhoneVerifyCode.equals(userPhoneVerifyCode)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 注册时用户获取验证码
     * @param phone 用户的手机号
     * @param
     */
    @ResponseBody
    @PostMapping("/sendPhone")
    public RegisterMode sendPhone(@RequestParam("phone") String phone, HttpServletRequest request) {
        //生产6位验证码
        String systemVerifyCode = RegisterAssUtil.getVerifySmsCode();
        String verifyCode = RegisterAssUtil.getVerifyCode(phone, systemVerifyCode);
        if (verifyCode == null) {
            return new RegisterMode("获取验证码失败");
        }
        HttpSession session = request.getSession();
        session.setAttribute(PHONE_KEY, systemVerifyCode);
        return new RegisterMode();
    }

    /**
     * 用户邮箱验证链接进行核实
     * @param emailVerifyCode 用户的邮箱验证码
     * @return
     */
    @GetMapping("/doVerifyEmail")
    public ModelAndView doVerifyEmail(@PathParam("emailVerifyCode") String emailVerifyCode,
                                      HttpServletRequest request,Model model) {
        HttpSession session = request.getSession();
        String verifyCode = (String) session.getAttribute("emailVerifyCode");
        if (!CheckUtils.checkUtils(verifyCode,emailVerifyCode)){
            model.addAttribute("message","邮箱验证失败");
            return new ModelAndView("common/error");
        }
        if(!verifyCode.equals(emailVerifyCode)){
            model.addAttribute("message","邮箱验证失败");
            return new ModelAndView("common/error");
        }
        model.addAttribute("message","邮箱验证成功");
        RegisterUserInfoMode userInfoMode =(RegisterUserInfoMode) session.getAttribute("user");
        userInfoMode.setUserEmailVerify(0);//0为邮箱已经验证
        session.setAttribute("user",userInfoMode);
        session.removeAttribute("emailVerifyCode");
        return new ModelAndView("index");
    }
}
