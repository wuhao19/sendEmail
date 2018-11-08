package com.wuhao.email.controller;
import com.wuhao.email.Enum.EmailCode;
import com.wuhao.email.domain.EmailMessage;
import com.wuhao.email.form.Message;
import com.wuhao.email.service.EmailService;
import com.wuhao.email.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
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
import javax.validation.Valid;
import java.io.*;

@Controller
@RequestMapping("/dome")
@Slf4j
public class EmailController {
    @Autowired
    private EmailService emailService;
    EmailMessage emailMessage = new EmailMessage();
    @GetMapping("/form")
    public ModelAndView form(){
        return new ModelAndView("form");
    }

    /**
     * 发送任何一封邮件
     * @param message dto传送的信息
     * @param multipartFile 传送的文件信息
     * @param model 返回的页面信息
     * @return 视图
     */
    @PostMapping("/send")
    public String send(@Valid Message message,
                             MultipartFile multipartFile,
                             Model model) {
        EmailCode emailCode = EmailUtil.isMessage(message, multipartFile);
        if (emailCode.getCode() == 100) {
            BeanUtils.copyProperties(message, emailMessage);
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
}
