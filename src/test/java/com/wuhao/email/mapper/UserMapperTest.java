package com.wuhao.email.mapper;

import com.wuhao.email.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void findUserByEmail() {
        User user = userMapper.findUserByEmail("928707094@qq.com");
        Assert.assertNotNull(user);
    }
    @Test
    public void updateUserInfoTest(){
        User user = userMapper.findUserByEmail("928707094@qq.com");
        user.setUserPassword("12121313554898");
        int i = userMapper.updateUserInfo(user);
        System.out.println(i);
        Assert.assertEquals(1,i);
    }
}