package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Address;
import com.wuhao.email.mapper.AddressMapper;
import com.wuhao.email.service.IAddressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Address> findAddressByUserId(int userId) {
        if (StringUtils.isBlank(String.valueOf(userId))){
            return null;
        }
        List<Address> addressList = addressMapper.selectList(new QueryWrapper<Address>().eq("uid", userId));
        if (addressList.size()==0){
            return null;
        }
       return addressList;
    }
}
