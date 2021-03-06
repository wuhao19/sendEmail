package com.wuhao.email.service;

import com.wuhao.email.domain.EmailMessage;

import java.util.concurrent.Future;

public interface EmailService {
     void sendSimpleEmail(EmailMessage emailMessage);
     void sendEnclosureFormEmail(EmailMessage emailMessage);
     void sendEnclosureEmail(String to,String form,String subject,String content,String rscPath,String rscId);

     /**
      * 发送验证邮件
      * @param emailTo 发送对象
      * @return 邮箱验证码
      */
     Future<Boolean> sendVerifyEmail(String emailTo) throws Exception;

}
