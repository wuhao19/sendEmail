package com.wuhao.email.service;

import com.wuhao.email.domain.EmailMessage;

public interface EmailService {
     void sendSimpleEmail(EmailMessage emailMessage);
     void sendEnclosureFormEmail(EmailMessage emailMessage);
     void endEnclosureEmail(String to,String form,String subject,String content,String rscPath,String rscId);

     /**
      * 发送验证邮件
      * @param emailTo 发送对象
      * @return 邮箱验证码
      */
     String sendVerifyEmail(String emailTo);

}
