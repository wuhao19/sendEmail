package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.LoginMode;
import com.wuhao.email.domain.User;
import com.wuhao.email.mapper.UserMapper;
import com.wuhao.email.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public boolean updateUserInfo(User user) {
        if (user==null){
            return false;
        }
        int result = userMapper.updateUserInfo(user);
        if (result!=1){
            return false;
        }
        return true;
    }

    /**
     * 用用户名查询一个用户
     * @param userName
     * @return
     */
    private User findUserByUserName(String userName) {
        User user = userMapper.findUserByUserName(userName);
        if (user==null){
            return null;
        }else {
            return user;
        }
    }
    /**
     * 用电话查询一个用户
     * @param userPhone
     * @return
     */
    private User findUserByPhone(String userPhone) {
        User user = userMapper.findUserByPhone(userPhone);
        if (user==null){
            return null;
        }else {
            return user;
        }
    }
    /**
     * 用邮箱查询一个用户
     * @param userEmail
     * @return
     */
    @Override
    public User findUserByEmail(String userEmail) {
        User user = userMapper.findUserByEmail(userEmail);
        if (user==null){
            return null;
        }else {
            return user;
        }
    }

    /**
     * 用户登录by邮箱
     * @param userEmail
     * @param password
     * @return
     */
    @Override
    public User loginByEmail(String userEmail, String password) {
        if (StringUtils.isBlank(userEmail)||StringUtils.isBlank(password)){
            return null;
        }
        User user = userMapper.findUserByEmail(userEmail);
        if(user==null){
            return null;
        }
        if (!user.getUserPassword().equals(password)){
            return null;
        }
        return user;
    }
    /**
     * 用户登录by电话
     * @param userPhone
     * @param password
     * @return
     */
    @Override
    public User loginByPhone(String userPhone, String password) {
        if (StringUtils.isBlank(userPhone)||StringUtils.isBlank(password)){
            return null;
        }
        User user = userMapper.findUserByPhone(userPhone);
        if(user==null){
            return null;
        }
        if (!user.getUserPassword().equals(password)){
            return null;
        }
        return user;
    }
    /**
     * 用户登录by用户名
     * @param userName
     * @param password
     * @return
     */
    @Override
    public User loginByUserName(String userName, String password) {
        if (StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
            return null;
        }
        User user = userMapper.findUserByUserName(userName);
        if(user==null){
            return null;
        }
        if (!user.getUserPassword().equals(password)){
            return null;
        }
        return user;
    }


    /**
     * 用户的注册过程
     * @param user
     * @return
     */
    @Override
    public boolean registerUserInfo(User user) {
        //判断用户是否为空
        if (user==null){
            return false;
        }
        //查询是否存在相同的用户名，邮箱和电话
        if(!checkUserExist(user)){
            return false;
        }
        //保存用户信息
        if (!saveUserInfo(user)){
            return false;
        }
        //设置用户的creatBy和updateBy 根据用户的phone
        if (!initCreatByAndUpdateBy(user.getUserPhone())){
            return false;
        }
        return true;
    }

    /**
     * 初始化用户的创建者和更新者
     * @param userPhone
     * @return
     */
    private boolean initCreatByAndUpdateBy(String userPhone){
        if (StringUtils.isBlank(userPhone)){
            return false;
        }
        User user = findUserByPhone(userPhone);
        //默认设置他自己为创建的人和更新的人
        user.setCrateBy(user.getUserId());
        user.setUpdateBy(user.getUserId());
        if (!updateUserInfo(user)){
            return false;
        }else {
            return true;
        }
    }
    /**
     * 保存用户的信息
     * @param user
     * @return
     */
    private boolean saveUserInfo(User user){
        if (user==null){
            return false;
        }
        int result = userMapper.insert(user);
        if(result!=1){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 检查数据库中是否有该用户的用户名，电话，邮箱
     * @param user
     * @return
     */
    private boolean checkUserExist(User user){//false 表示数据库中有这条记录  true 表示数据库中没有
        if (user==null){
            return false;
        }
        if (checkUserName(user.getUserName())){return false;}
        if (checkUserEmail(user.getUserEmail())){return false;}
        if (checkUserPhone(user.getUserPhone())){return false;}
        return true;
    }

    /**
     * 检查是否有相同的用户名
     * @param userName
     * @return
     */
    private boolean checkUserName(String userName){
        User user = findUserByUserName(userName);
        if (user==null){
            return false;
        }else {
            return true;
        }
    }
    /**
     * 检查是否有相同的邮箱
     * @param userEmail
     * @return
     */
    private boolean checkUserEmail(String userEmail){
        User user = findUserByEmail(userEmail);
        if (user==null){
            return false;
        }else {
            return true;
        }
    }
    /**
     * 检查是否有相同的手机号
     * @param userPhone
     * @return
     */
    private boolean checkUserPhone(String userPhone){
        User user = findUserByPhone(userPhone);
        if (user==null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public User doLogin(String userName, String password, LoginMode loginMode) {
        if (StringUtils.isBlank(userName)||StringUtils.isBlank(password)||loginMode==null){
            return null;
        }
        User user;
        switch (loginMode.getLoginType()){
            case 1://邮箱登录
                user=loginByEmail(userName,password);
                break;
            case 2://电话登录
                user = loginByPhone(userName,password);
                break;
            case 3://用户名登录
                user = loginByUserName(userName,password);
                break;
                default:
                    user = null;
                    break;
        }
        return user;
    }
}
