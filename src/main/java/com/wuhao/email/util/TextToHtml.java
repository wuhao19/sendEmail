package com.wuhao.email.util;

import com.wuhao.email.domain.EmailMessage;

public class TextToHtml {
    public static String getHtml(EmailMessage emailMessage){
        String htmlTemp = "<html><body><p>"+emailMessage.getText()+"</p><hr/><img src=\'cid:"+emailMessage.getSrcId()+"\'></body></html>";
        return htmlTemp;
    }
}
