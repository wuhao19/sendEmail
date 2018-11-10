package com.wuhao.email.controller;

import com.wuhao.email.domain.*;
import com.wuhao.email.service.EmailService;
import com.wuhao.email.service.UserService;
import com.wuhao.email.util.RegisterAssUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

    private static final String PHONE_KEY = "PHONE_KEY";
    public static final String EX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String EX_PHONE = "^[1][34578]\\d{9}$";
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
        boolean checkPhoneVerifyCodeIsEmpty = checkPhoneVerifyCode(systemVerifyCode, userInfoMode.getPhoneCode());
        if (!checkPhoneVerifyCodeIsEmpty) {
            setRegeditError(model, "验证码输入错误", userInfoMode);
            return "register/register";
        }
        //验证成功
        // Service保存用户信息
        RegisterUserInfoMode registerUserInfoMode = new RegisterUserInfoMode();
        BeanUtils.copyProperties(userInfoMode, registerUserInfoMode);
        System.out.println(registerUserInfoMode);
        // 清除Session 手机验证码
        session.removeAttribute(PHONE_KEY);
        session.setAttribute("user", registerUserInfoMode);
        return "index";
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
     * 检查注册时返回的Register是否为空
     *
     * @param registerMode 检查的对象
     * @param userInfoMode 用户信息
     * @return 返回路径
     */
    private boolean checkResultIsNull(RegisterMode registerMode, RegisterUserInfoMode userInfoMode) {
        if (registerMode == null || registerMode.getCode() != 0) {
            if (userInfoMode == null) {
                userInfoMode = new RegisterUserInfoMode();
            }
            return false;
        }
        return true;
    }

    /**
     * 检查用户的信息是否合法
     *
     * @param userInfoMode 用户的信息
     * @return boolean
     */
    private String checkUserInfoEnpty(RegisterUserInfoMode userInfoMode) {
        String message = null;

        if (userInfoMode == null) {
            return message = "用户不能为空或者输入格式有误";
        }
        if(StringUtils.isBlank(userInfoMode.getUserName()) || userInfoMode.getUserName().length()>=MAX_VALUE){
            return message = "用户名不能为空或者输入格式有误";
        }
        if (StringUtils.isBlank(userInfoMode.getUserPassword()) || userInfoMode.getUserPassword().length()>=MAX_VALUE) {
            return message = "用户的密码不能为空或者输入的密码格式有误";
        }
        if (StringUtils.isBlank(userInfoMode.getUserEmail()) || userInfoMode.getUserEmail().length()>=MAX_VALUE) {
            return message = "用户的Email不能为空或者输入的格式有误";
        }
        if (StringUtils.isBlank(userInfoMode.getUserPhone()) || userInfoMode.getUserPhone().length()>=MAX_VALUE) {
            return message = "用户的手机号不能为空或者手机号输入错误";
        }
        if (!userInfoMode.getUserEmail().matches(EX_EMAIL)) {
            return message = "用户的Email不合法";
        }
        if (!userInfoMode.getUserPhone().matches(EX_PHONE)) {
            return message = "用户的手机号不合法";
        }
        if (StringUtils.isBlank(userInfoMode.getPhoneCode()) || userInfoMode.getPhoneCode().length()>=MAX_VALUE) {
            return message = "用户输入的验证码格式不正确";
        }
        return null;
    }

    /**
     * 检查用户的手机验证码是否正确
     *
     * @param systemPhoneVerifyCode 系统验证码
     * @param userPhoneVerifyCode   用户输入的验证码
     * @return 验证错误信息对象
     */
    private boolean checkPhoneVerifyCode(String systemPhoneVerifyCode, String userPhoneVerifyCode) {
        if (StringUtils.isBlank(systemPhoneVerifyCode) || StringUtils.isBlank(userPhoneVerifyCode)) {
            return false;
        }
        if (!systemPhoneVerifyCode.equals(userPhoneVerifyCode)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 注册失败
     *
     * @param registerUserInfoMode 用户的注册信息
     * @param model                返回前段的model对象
     * @return 页面路径
     */
    private String failRegister(RegisterUserInfoMode registerUserInfoMode, Model model) {
        if (registerUserInfoMode == null) {
            registerUserInfoMode = new RegisterUserInfoMode();
        }
        model.addAttribute("user", registerUserInfoMode);
        return "register/register";
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
     * 用主页进行邮箱验证
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/sendEmail")
    public void sendEmailVerifyLink(HttpServletRequest request) {
        EmailMessage emailMessage = new EmailMessage();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        emailMessage.setForm("928707094@qq.com");
        emailMessage.setTo(user.getUserEmail());
        emailMessage.setSubject("邮箱认证");
        String emailText = "这是一封验证邮件,恭喜您注册成功，请点击下面的连接进行激活账户: http://127.0.0.1:8080/register/doVerifyEmail?userName=" + user.getUserName() + "&userEmail=" + user.getUserEmail();
        System.out.println(emailText);
        emailMessage.setText(emailText);
        emailService.sendHtmlEmail(emailMessage);
    }


    /**
     * 用户邮箱验证链接进行核实
     *
     * @param userName
     * @param userEmail
     * @return
     */
    @PostMapping("/doVerifyEmail")
    public ModelAndView doVerifyEmail(@RequestParam("userName") String userName, @RequestParam("userEmail") String userEmail,
                                      Map<String, Object> map
    ) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(userEmail)) {
            map.put("message", "用户或者邮箱不能为空");
            return new ModelAndView("common/error", map);
        }
        //调动UserService 查询是否用户
        User user = userService.findUserByEmail(userEmail);
        if (null == user || !user.getUserName().equals(userName)) {
            map.put("message", "验证失败！该用户不存在。");
            return new ModelAndView("common/error", map);
        }
        user.setUserEmailVerify(0);
        userService.updateUser(user);
        map.put("message", "邮箱验证成功");
        return new ModelAndView("common/success", map);
    }
}
