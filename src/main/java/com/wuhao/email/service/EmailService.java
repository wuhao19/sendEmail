package com.wuhao.email.service;

import com.wuhao.email.domain.EmailMessage;

public interface EmailService {
     void sendSimpleEmail(EmailMessage emailMessage);
     void sendHtmlEmail(EmailMessage emailMessage);
     void sendEnclosureFormEmail(EmailMessage emailMessage);
     void endEnclosureEmail(String to,String form,String subject,String content,String rscPath,String rscId);
}
