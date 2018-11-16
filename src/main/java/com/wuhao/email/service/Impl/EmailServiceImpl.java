package com.wuhao.email.service.Impl;

import com.wuhao.email.domain.EmailMessage;
import com.wuhao.email.domain.Event;
import com.wuhao.email.domain.User;
import com.wuhao.email.domain.Verify;
import com.wuhao.email.service.EmailService;
import com.wuhao.email.service.IEventService;
import com.wuhao.email.service.IUserService;
import com.wuhao.email.service.IVerifyService;
import com.wuhao.email.util.EmailUtil;
import com.wuhao.email.util.TextToHtml;
import com.wuhao.email.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.concurrent.Future;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value("${myConfig.emailSendFrom}")
    private String emailFrom;
    @Value("${myConfig.emailSubject}")
    private String emailSubject;
    @Value("${myConfig.emailText}")
    private String emailText;
    @Value("${myConfig.emailVerifyPath}")
    private String emailVerifyPath;

    public static final int EMAIL_VERIFY=1;

    public static final String EVENT_EMAIL_MODEL="发送验证邮件";
    public static final String EVENT_TYPE="邮件";
    public static final String EVENT_EMAIL_ERROR="获取验证邮件时失败了";
    public static final String EVENT_EMAIL_SUCCESS="获取了验证邮件";

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private IEventService eventService;
    @Autowired
    private IVerifyService verifyService;
    @Autowired
    private IUserService userService;

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
    public void sendEnclosureEmail(String to, String form, String subject, String content, String rscPath, String rscId) {
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
    @Async
    @Override
    public Future<Boolean> sendVerifyEmail(String emailTo) {
        if(StringUtils.isBlank(emailTo)){
            log.error(EVENT_EMAIL_ERROR);
            return new AsyncResult<Boolean>(false);
        }
        String emailVerifyCode = EmailUtil.getEmailVerifyCode();//获取邮件验证码
        User user = userService.findUserByEmail(emailTo); //根据邮箱获取用户
        if(!saveEmailVerifyCode(user.getUserId(), emailVerifyCode)){//保存邮箱验证码 用于验证邮箱是否过期
            log.error(EVENT_EMAIL_ERROR);
            return new AsyncResult<Boolean>(false);
        }
        try {
            if(!initAndSendVerifyEmail(emailTo, emailVerifyCode)){ //构造邮件并发送
                log.error(EVENT_EMAIL_ERROR);
                return new AsyncResult<Boolean>(false);
            }
        } catch (MessagingException e) {
            //记录邮件发送异常的事件
            log.error("邮件发送失败：{}",e.getMessage());
            recordEmailErrorEvent(EVENT_EMAIL_ERROR, user.getUserId());
            log.error(EVENT_EMAIL_ERROR);
            return new AsyncResult<Boolean>(false);
        }finally {
            //TODO 进行记录返回值判断
            recordEmailErrorEvent(EVENT_EMAIL_SUCCESS, user.getUserId());
            return new AsyncResult<Boolean>(false);
        }
    }

    /**
     * 构造验证邮件并发送
     * @param emailTo
     * @param emailVerifyCode
     * @return
     * @throws MessagingException
     */
    private boolean initAndSendVerifyEmail(String emailTo,String emailVerifyCode) throws MessagingException{
        if (StringUtils.isBlank(emailTo)||StringUtils.isBlank(emailVerifyCode)){
            return false;
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(emailFrom);
        helper.setSubject(emailSubject);
        helper.setTo(emailTo);
        helper.setText(emailText+emailVerifyPath+emailVerifyCode,true);
        javaMailSender.send(message);
        return true;
    }
    /**
     * 记录邮件验证失败的事件
     * @param message
     * @param userId
     * @return
     */
    private void recordEmailErrorEvent(String message,int userId){
        if(StringUtils.isBlank(message)|| userId==0){
            log.warn("验证邮件发送事件记录时为空");
            return;
        }
        Event event = new Event();
        event.setCrateBy(userId);
        event.setUpdateBy(userId);
        event.setEventModel(EVENT_EMAIL_MODEL);
        event.setUid(userId);
        event.setEventType(EVENT_TYPE);
        event.setEventData(message);
        event.setEventTime(new Date());
        if (!eventService.saveEvent(event)){
            log.warn("验证邮件发送事件记录时失败");
        }
    }
    /**
     * 构建邮箱验证对象保存数据库
     * @param userId
     * @param emailVerifyCode
     * @return
     */
    private boolean saveEmailVerifyCode(int userId,String emailVerifyCode){
        if(StringUtils.isBlank(emailVerifyCode)||userId==0){
            return false;
        }
        Verify verify = new Verify();
        verify.setOverTime(TimeUtils.getEmailVerifyExitTime());//获取当前时间的后一天
        verify.setUid(userId);
        verify.setContent(emailVerifyCode);
        verify.setType(EMAIL_VERIFY);
        verify.setCreatBy(userId);
        verify.setUpdateBy(userId);
        if (!verifyService.saveVerifyCode(verify)){
            return false;
        }
        return true;
    }
}
