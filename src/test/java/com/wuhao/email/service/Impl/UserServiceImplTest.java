package com.wuhao.email.service.Impl;

import com.wuhao.email.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private IUserService userService;
    @Test
    public void login() {
    }

//    @Test
//    public void findUserById() {
//        User userByPhone = userService.findUserByPhone("17723938580");
//        Assert.assertNotNull(userByPhone);
//    }

    @Test
    public void findUserByPhone() {
    }

    @Test
    public void keepUserInfo() {
    }
}