package com.wuhao.email.service.Impl;

import com.wuhao.email.domain.User;
import com.wuhao.email.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    /**
     * 用户的登录过程
     * @param userName
     * @param passWord
     * @return
     */
    @Override
    public User login(String userName, String passWord) {
        User user  = new User();
        user.setUserName("哈沙河");
        return user;
    }

    /**
     * 用户的注册过程
     * @param user
     * @return
     */
    @Override
    public User register(User user) {
        // 数据库保存成功，返回一个User对象
        return user;
    }
    /**
     * 邮箱验证是使用邮箱查找用户
     * @param userEmail
     * @return
     */
    @Override
    public User findUserByEmail(String userEmail) {
        User user = new User();
        user.setUserName("wuhao");
        return user;
    }
    /**
     * 更新用户的邮箱验证状态
     * @param user
     */
    @Override
    public void updateUser(User user) {
    }


}
