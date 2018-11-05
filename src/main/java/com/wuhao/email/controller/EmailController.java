package com.wuhao.email.controller;
import com.wuhao.email.domain.EmailMessage;
import com.wuhao.email.form.Message;
import com.wuhao.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.Map;

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

    @PostMapping("/send")
    public ModelAndView send(@Valid Message message,
                             MultipartFile multipartFile,
                             Map<String,Object> map) {
        BeanUtils.copyProperties(message, emailMessage);
        if (!multipartFile.isEmpty()) {
            File temp = new File("");
            String filePath = temp.getAbsolutePath() + "\\" + multipartFile.getOriginalFilename();
            BufferedOutputStream out;
            try {
                out = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                out.write(multipartFile.getBytes());
                out.flush();
                out.close();
                emailMessage.setFilePath(filePath);
                emailMessage.setFileName(multipartFile.getOriginalFilename());
                emailService.sendEnclosureFormEmail(emailMessage);
                return new ModelAndView("success");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else {
            emailService.sendSimpleEmail(emailMessage);
            return new ModelAndView("success");
        }
        return new ModelAndView("error");
    }
}
