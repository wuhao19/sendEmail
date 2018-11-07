package com.wuhao.email.service.Impl;

import com.wuhao.email.domain.User;
import com.wuhao.email.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Override
    public User login(String userName, String passWord) {
        User user  = new User();
        user.setUserName("哈沙河");
        return user;
    }
}
