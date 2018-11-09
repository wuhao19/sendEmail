package com.wuhao.email.domain;

import lombok.Data;

/**
 * 用户注册的信息刷
 */
@Data
public class RegisterUserInfoMode {

    private String userName;

    private String userPassword;

    private String userEmail;

    private String userPhone;

    private String phoneCode;

    /**
     * 用户的手机验证  0为已经验证 1为未验证 默认为0
     */
    private int userPhoneVerify=0;
    /**
     * 用户的邮箱验证  0为已经验证 1为未验证 默认为1
     */
    private int userEmailVerify=1;
}
