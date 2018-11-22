package com.wuhao.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.Address;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-20
 */
public interface IAddressService extends IService<Address> {
   List<Address> findAddressByUserId(int userId);

}
