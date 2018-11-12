package com.wuhao.email.controller;
import com.wuhao.email.Enum.EmailCode;
import com.wuhao.email.domain.EmailMessage;
import com.wuhao.email.domain.User;
import com.wuhao.email.domain.Verify;
import com.wuhao.email.service.EmailService;
import com.wuhao.email.service.IUserService;
import com.wuhao.email.service.IVerifyService;
import com.wuhao.email.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/email")
@Slf4j
public class EmailController {
    public static final String EMAIL_VERIFY_ERROR="邮箱验证失败";
    public static final String EMAIL_VERIFY_SUCCESS="邮箱验证成功";
    public static final String JUMP_ERROR="common.error";
    public static final String JUMP_SUCCESS="common.success";
    public static final String MESSAGE="message";


    @Autowired
    private EmailService emailService;
    @Autowired
    private IVerifyService verifyService;
    @Autowired
    private IUserService userService;

    @GetMapping("/form")
    public ModelAndView form(){
        return new ModelAndView("form");
    }

    /**
     * 发送任何一封邮件
     * @param emailMessage dto传送的信息
     * @param multipartFile 传送的文件信息
     * @param model 返回的页面信息
     * @return 视图
     */
    @PostMapping("/send")
    public String send(@Valid EmailMessage emailMessage,
                             MultipartFile multipartFile,
                             Model model) {
        EmailCode emailCode = EmailUtil.isMessage(emailMessage, multipartFile);


        if (emailCode.getCode() == 100) {
            BeanUtils.copyProperties(emailMessage, emailMessage);
            if (!multipartFile.isEmpty()) {
                try {
                    emailMessage.setFilePath(EmailUtil.getFilePath(multipartFile));
                    emailMessage.setFileName(MimeUtility.encodeText(multipartFile.getOriginalFilename()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                emailService.sendEnclosureFormEmail(emailMessage);
                model.addAttribute("code",emailCode.getCode());
                model.addAttribute("message",emailCode.getMessage());
                return "common/success";
            } else {
                emailService.sendSimpleEmail(emailMessage);
                model.addAttribute("code",emailCode.getCode());
                model.addAttribute("message",emailCode.getMessage());
                return "common/success";
            }
        }else {
            model.addAttribute("code",emailCode.getCode());
            model.addAttribute("message",emailCode.getMessage());
            return "common/error";
        }
    }
    /**
     * 用户邮箱验证链接进行核实
     * @param emailVerifyCode 用户的邮箱验证码
     * @return
     */
    @GetMapping("/doVerifyEmail")
    public ModelAndView doVerifyEmail(@PathParam("emailVerifyCode") String emailVerifyCode,
                                      HttpSession session, Model model) {

        //检查用户连接邮箱验证码
        if(!checkEmailVerifyCode(emailVerifyCode)){
          return resultModel(false,model);
        }
        //获取用户保存数据库的邮箱验证码
        User user = (User) session.getAttribute("user");
        String verifyCodeFromMySql = getEmailVerifyCodeFromMySql(user);
        //对比验证码的属性
        if (!checkTowVerifyCode(emailVerifyCode,verifyCodeFromMySql)){
            return resultModel(false,model);
        }
        //TODO 删除数据库中保存的邮件验证码
        //修改用户的邮箱验证状态并保持到数据库
        user.setEmailStatus(0);//0为已经验证
        if (!updateUserEmailStatus(user)) {
            return resultModel(false,model);
        }
        //验证成功，返回session数据和用户数据
        session.setAttribute("user",user);
        return resultModel(true,model);
    }

    /**
     * 更新用户的邮箱验证状态
     * @param user
     * @return
     */
    private boolean updateUserEmailStatus(User user){
        if (user==null){
            return false;
        }
        if (!userService.updateUserInfo(user)) {
            return false;
        }
        return true;
    }
    /**
     * 用户验证码和数据库验证码对比
     * @param emailVerifyCode
     * @param verifyCodeFromMySql
     * @return
     */
    private boolean checkTowVerifyCode(String emailVerifyCode,String verifyCodeFromMySql){
        if (StringUtils.isBlank(emailVerifyCode)||StringUtils.isBlank(verifyCodeFromMySql)){
            return false;
        }
        if (!emailVerifyCode.equals(verifyCodeFromMySql)){
            return false;
        }
        return true;
    }

    /**
     * 获取用户在数据保存的验证码
     * @param user
     * @return
     */
    private String getEmailVerifyCodeFromMySql(User user){
           if (user==null){
               return null;
           }
            Verify verify= verifyService.findVerifyByUserId(user.getUserId());
           if (verify==null){
               return null;
           }
           if (verify.getContent()==null){
               return null;
           }
           return verify.getContent();
    }
    /**
     * 用户验证返回页面的封装
     * @param resultPage
     * @param model
     * @return
     */
    private ModelAndView resultModel(boolean resultPage,Model model){
        if (model==null){
            return null;
        }
        if (resultPage = false){
            model.addAttribute(MESSAGE,EMAIL_VERIFY_ERROR);
            return new ModelAndView(JUMP_ERROR);
        }else {
            model.addAttribute(MESSAGE,EMAIL_VERIFY_SUCCESS);
            return new ModelAndView(JUMP_SUCCESS);
        }
    }
    /**
     * 检查用户邮箱连接验证码是否合法
     * @param emailVerifyCode
     * @return
     */
    private boolean checkEmailVerifyCode(String emailVerifyCode){
        if(StringUtils.isBlank(emailVerifyCode)){
            return false;
        }
        return true;
    }
}

