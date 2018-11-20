package com.wuhao.email.service.Impl;

import com.wuhao.email.domain.Order;
import com.wuhao.email.mapper.OrderMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {
    @Autowired
    private OrderMapper orderMapper;
    @Test
    public void crateOrder() {
        Order order=new Order();
        order.setId("546454848");//设置订单编号

        int insert = orderMapper.insert(order);
        System.out.println(insert);
        Assert.assertEquals(1,insert);
    }
}