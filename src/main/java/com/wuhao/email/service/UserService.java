package com.wuhao.email.service;

import com.wuhao.email.domain.User;

import java.util.List;

public interface UserService {

    /***
     * 用户登录过程
     * @param userName
     * @param passWord
     * @return
     */
    User login(String userName,String passWord);


}
