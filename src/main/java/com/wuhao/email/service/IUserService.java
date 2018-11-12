package com.wuhao.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
public interface IUserService extends IService<User> {
    /**
     * 用户的注册过程
     * @param user
     * @return
     */
    boolean registerUserInfo(User user);
    User findUserByEmail(String userEmail);

    /**
     * 用户的3种登录方式
     * @param userEmail
     * @param password
     * @return
     */
    User loginByEmail(String userEmail,String password);
    User loginByPhone(String userPhone,String password);
    User loginByUserName(String userName,String password);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    boolean updateUserInfo(User user);
}
