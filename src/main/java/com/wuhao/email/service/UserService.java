package com.wuhao.email.service;

import com.wuhao.email.domain.User;

public interface UserService {

    /***
     * 用户登录过程
     * @param userName
     * @param passWord
     * @return
     */
    User login(String userName,String passWord);

    /**
     * 用户的注册过程
     * @param user
     * @return
     */
    User register(User user);

    /**
     * 邮箱验证是使用邮箱查找用户
     * @param userEmail
     * @return
     */
    User findUserByEmail(String userEmail);

    /**
     * 更新用户的邮箱验证状态
     * @param user
     */
    void updateUser(User user);


}
