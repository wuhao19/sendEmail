package com.wuhao.email.service.Impl;

import com.wuhao.email.domain.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.Assert.*;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceImplTest {
    EmailMessage message = new EmailMessage();
    @Autowired
    EmailServiceImpl emailService;
    @Test
    public void sendSimpleEmail() {
        message.setForm("928707094@qq.com");
        message.setTo("928707094@qq.com");
        message.setSubject("你好！");
        message.setText("这是一封简单的邮件！");
        emailService.sendSimpleEmail(message);
    }
    @Test
    public void sendHtmlEmail(){
        message.setForm("928707094@qq.com");
        message.setTo("928707094@qq.com");
        message.setSubject("你好！");
        message.setText("<html> <br/>" +
                "<body><br/>" +
                "<a href='www.baidu.com'>www.baidu.com</a><br/>" +
                "</body><br/>" +
                "</html>");
        emailService.sendHtmlEmail(message);
    }
    @Test
    public void sendEnclosureFormEmail(){
        message.setForm("928707094@qq.com");
        message.setTo("928707094@qq.com");
        message.setSubject("你好！");
        message.setText("232323");
        message.setFilePath("E:\\workplace\\J2EE\\email\\src\\main\\resources\\static\\2323.jpg");
        message.setFileName("2323.jpg");
        emailService.sendEnclosureFormEmail(message);
    }
    @Test
    public void endEnclosureEmail(){
        message.setForm("928707094@qq.com");
        message.setTo("928707094@qq.com");
        message.setSubject("你好！");
        String imgPath="E:\\workplace\\J2EE\\email\\src\\main\\resources\\static\\2323.jpg";
        String rscId="2323.jpg";
        String content="<html><body><p>这是发给你的的图片!</p><hr/><img src=\'cid:"+rscId+"\'></body></html>";
        emailService.endEnclosureEmail(message.getTo(),message.getForm(),message.getSubject(),content,imgPath,rscId);
    }
}