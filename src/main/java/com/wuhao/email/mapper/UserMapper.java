package com.wuhao.email.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuhao.email.domain.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user WHERE user_email=#{userEmail}")
    User findUserByEmail(String userEmail);

    @Select("SELECT * FROM user WHERE user_phone=#{userPhone}")
    User findUserByPhone(String userPhone);

    @Select("SELECT * FROM user WHERE user_name=#{userName}")
    User findUserByUserName(String userName);

    @Update("UPDATE user SET " +
            "user_name=#{userName}," +
            "user_password=#{userPassword}," +
            "user_email=#{userEmail}," +
            "user_phone=#{userPhone}," +
            "email_status=#{emailStatus}," +
            "phone_status=#{phoneStatus}," +
            "user_icon=#{userIcon}," +
            "crate_by=#{crateBy}," +
            "update_by=#{updateBy} WHERE user_id=#{userId}")
    int updateUserInfo(User user);

}
