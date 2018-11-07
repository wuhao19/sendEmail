package com.wuhao.email.domain;

import lombok.Data;

@Data
public class User {
    private Integer userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userPhone;
    private String userImg;
}
