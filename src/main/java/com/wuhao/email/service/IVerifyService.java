package com.wuhao.email.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.Verify;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
public interface IVerifyService extends IService<Verify> {
     boolean saveVerifyCode(Verify verify);
}
