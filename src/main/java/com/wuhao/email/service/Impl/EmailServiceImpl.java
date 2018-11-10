package com.wuhao.email.service.Impl;
import com.wuhao.email.domain.EmailMessage;
import com.wuhao.email.service.EmailService;
import com.wuhao.email.util.EmailUtil;
import com.wuhao.email.util.TextToHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${myConfig.emailSendFrom}")
    private String emailFrom;
    @Value("${myConfig.emailSubject}")
    private String emailSubject;
    @Value("${myConfig.emailText}")
    private String emailText;
    @Value("${myConfig.emailVerifyPath}")
    private String emailVerifyPath;

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * 发送一份简单的邮件
     * @param emailMessage
     */
    @Override
    public void sendSimpleEmail(EmailMessage emailMessage) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailMessage.getForm());
        mailMessage.setSubject(emailMessage.getSubject());
        mailMessage.setTo(emailMessage.getTo());
        mailMessage.setText(emailMessage.getText());
        javaMailSender.send(mailMessage);
    }


    /**
     * 发送一份表单邮件，判断附件信息，图片展示
     * @param emailMessage
     */
    @Override
    public void sendEnclosureFormEmail(EmailMessage emailMessage) {
        MimeMessage message = javaMailSender.createMimeMessage();
        System.setProperty("mail.mime.splitlongparameters","false");
        try {
            String fileName = emailMessage.getFileName();
            String filePath = emailMessage.getFilePath();
            emailMessage.setSrcId(fileName);
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(emailMessage.getForm());
            helper.setTo(emailMessage.getTo());
            helper.setSubject(emailMessage.getSubject());
            FileSystemResource res = new FileSystemResource(new File(filePath));
            fileName=fileName.toLowerCase();
            if (fileName.endsWith(".jpg")||fileName.endsWith(".png")){
                String content = TextToHtml.getHtml(emailMessage);
                helper.setText(content,true);
                helper.addInline(emailMessage.getSrcId(),res);
                javaMailSender.send(message);
            }else {
                helper.setText(emailMessage.getText(),true);
                helper.addAttachment(fileName,res);
                javaMailSender.send(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void endEnclosureEmail(String to, String form, String subject, String content, String rscPath, String rscId) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(form);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId,res);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送验证邮件
     * @param emailTo 发送对象
     */
    @Override
    public String sendVerifyEmail(String emailTo) {
        MimeMessage message = javaMailSender.createMimeMessage();
        //获取邮件验证码
        String emailVerifyCode = EmailUtil.getEmailVerifyCode();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(emailFrom);
            helper.setSubject(emailSubject);
            helper.setTo(emailTo);
            helper.setText(emailText+emailVerifyPath+emailVerifyCode,true);
            javaMailSender.send(message);
            return emailVerifyCode;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
