package com.wuhao.email.domain;

import lombok.Data;

@Data
public class User {
    private Integer userId;
    private String userName;
    private String userPassword;

    private String userEmail;
    /**
     * 用户的邮箱验证  0为已经验证 1为未验证 默认为1
     */
    private int userEmailVerify=1;

    private String userPhone;
    /**
     * 用户的手机验证  0为已经验证 1为未验证 默认为0
     */
    private int userPhoneVerify=0;
    private String userImg;
}
