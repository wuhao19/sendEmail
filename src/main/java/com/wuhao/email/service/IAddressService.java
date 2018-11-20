package com.wuhao.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.Address;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-20
 */
public interface IAddressService extends IService<Address> {
    String findAddressByUserId(int userId);

}
