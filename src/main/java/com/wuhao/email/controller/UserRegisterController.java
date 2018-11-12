package com.wuhao.email.controller;

import com.wuhao.email.domain.RegisterMode;
import com.wuhao.email.domain.User;
import com.wuhao.email.dto.UserDto;
import com.wuhao.email.service.EmailService;
import com.wuhao.email.service.IEventService;
import com.wuhao.email.service.IUserService;
import com.wuhao.email.util.CheckUtils;
import com.wuhao.email.util.DtoTOPojoUtils;
import com.wuhao.email.util.RegisterAssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/register")
@Slf4j

public class UserRegisterController {

    private static final String PHONE_KEY = "PHONE_KEY";
    public static final int MAX_VALUE = 128;
    public static final int PHONE_VERIFY_SUCCESS=0;//0表示手机已经验证
    public static final String SEND_VERIFY_EMAIL_ERROR="邮箱验证发送失败";

    @Autowired
    private IUserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IEventService eventService;

    @GetMapping("/")
    public String register(Model model) {
        UserDto userInfo = new UserDto();
        model.addAttribute("user", userInfo);
        return "register/register";
    }

    /**
     * 注册用户提交的信息页面
     *
     * @param userInfo 获取用户输入信息
     * @param model        返换用户的对象
     * @param request      request请求
     * @return
     */
    @RequestMapping("/doRegister")
    public String doRegister(@Valid UserDto userInfo, Model model, HttpServletRequest request) {
        // 检查用户数据
        String checkRegisterUserInfo = checkUserInfoEnpty(userInfo);
        if (checkRegisterUserInfo != null) {
            setRegisterError(model, checkRegisterUserInfo, userInfo);
            return "register/register";
        }
        //获取手机验证码
        HttpSession session = request.getSession();
//        String systemVerifyCode = (String) session.getAttribute(PHONE_KEY);
        //检查用户手机的验证码
//        if (!checkPhoneVerifyCode(systemVerifyCode, userInfo.getPhoneVerifyCode())) {
//            setRegisterError(model, "验证码输入错误", userInfo);
//            return "register/register";
//        }
        //保存用户信息
        User user = saveUserInfo(userInfo);
        // 清除Session 手机验证码
//        session.removeAttribute(PHONE_KEY);
        session.setAttribute("user",user);
        //发送邮箱验证 异步调用
        asyncSendVerifyEmail(userInfo.getUserEmail());
        return "login/login";
    }

    /**
     * 异步执行验证邮件发送
     * @param userEmail
     */
    private void asyncSendVerifyEmail(String userEmail){

        Future<Boolean> result = null;
        try {
             result = emailService.sendVerifyEmail(userEmail);
            while (true){
                if(result.isDone()) {
                    log.info("邮件发送线程异步调用执行成功！");
                    break;
                }
                if (result.isCancelled()){
                    log.info("邮件发送线程异步调用执行失败！");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存用户新用户信息
     * @param userInfo
     */
    private User saveUserInfo(UserDto userInfo){
        User user = DtoTOPojoUtils.dtoToPojo(userInfo);
        user.setPhoneStatus(PHONE_VERIFY_SUCCESS);
        if(!userService.registerUserInfo(user)){
            log.error("用户信息保存失败");
            return null;
        }else {
            return user;
        }
    }

    /**
     * 用户进行手动获取邮箱验证
     * @param request
     */
    @PostMapping("/sendEmail")
    public void sendEmail(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        User userInfo =(User) session.getAttribute("user");
        asyncSendVerifyEmail(userInfo.getUserEmail());
    }

    /**
     * 注册过程中出现错误封装返回的数据格式
     *
     * @param model 返换用户的对象
     * @param msg 返回页面的message信息
     * @param userInfo 用户的model对象
     */
    private void setRegisterError(Model model, String msg, UserDto userInfo) {
        model.addAttribute("message", msg);
        model.addAttribute("user", userInfo);
    }

    /**
     * 检查用户的信息是否合法
     * @param userInfo 用户的信息
     * @return boolean
     */
    private String checkUserInfoEnpty(UserDto userInfo) {
        String message = null;

        if (userInfo == null) {
            return message = "用户不能为空或者输入格式有误";
        }
        if(!CheckUtils.checkUtils(userInfo.getUserName())){
            return message = "用户名不能为空或者输入格式有误";
        }
        if (!CheckUtils.checkUtils(userInfo.getUserPassword())) {
            return message = "用户的密码不能为空或者输入的密码格式有误";
        }
        if (!CheckUtils.checkEmial(userInfo.getUserEmail())) {
            return message = "用户的Email不能为空或者输入的格式有误";
        }
        if (!CheckUtils.checkPhone(userInfo.getUserPhone())) {
            return message = "用户的手机号不能为空或者手机号输入错误";
        }
        if (!CheckUtils.checkUtils(userInfo.getPhoneVerifyCode())) {
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

}
