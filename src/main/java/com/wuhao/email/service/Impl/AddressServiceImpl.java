package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Address;
import com.wuhao.email.excptionHandler.MyException;
import com.wuhao.email.mapper.AddressMapper;
import com.wuhao.email.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-20
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public String findAddressByUserId(int userId) {
        Address address = addressMapper.selectOne(new QueryWrapper<Address>().eq("uid", userId));
        if (address==null){
            throw new MyException(123,"你还没有添加地址");
        }
        return address.getAddress();
    }
}
