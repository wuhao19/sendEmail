package com.wuhao.email.domain;
import lombok.Data;

@Data
public class EmailMessage {
    private String form = "928707094@qq.com";
    private String to;
    private String subject;
    private String text;
    private String filePath;
    private String fileName;
    private String srcId;
}
