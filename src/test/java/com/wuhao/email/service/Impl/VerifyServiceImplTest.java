package com.wuhao.email.service.Impl;

import com.wuhao.email.domain.Verify;
import com.wuhao.email.mapper.VerifyMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
@RunWith(SpringRunner.class)
@SpringBootTest
public class VerifyServiceImplTest {
    @Autowired
    VerifyMapper verifyMapper;

    @Test
    public void saveVerifyCode() {
        Verify verify = new Verify();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);//+1今天的时间加一天
        date = calendar.getTime();
        verify.setUid(18);
        verify.setContent("626626262626");
        verify.setType(1);
        verify.setCreatBy(18);
        verify.setUpdateBy(18);
        verify.setOverTime(date);
        int insert = verifyMapper.insert(verify);
        System.out.println(insert);
    }
}