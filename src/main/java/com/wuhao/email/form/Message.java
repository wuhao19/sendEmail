package com.wuhao.email.form;

import lombok.Data;

@Data
public class Message {
    private String to;
    private String subject;
    private String text;
    private  String filePath;
}
