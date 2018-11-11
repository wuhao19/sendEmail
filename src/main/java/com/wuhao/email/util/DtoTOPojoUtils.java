package com.wuhao.email.util;

import com.wuhao.email.domain.User;
import com.wuhao.email.dto.UserDto;
import org.springframework.beans.BeanUtils;

public class DtoTOPojoUtils {
    /**
     * Dto转换为Pojo
     * @param userInfo Dto
     * @return
     */
    public static User dtoToPojo(UserDto userInfo){
        if(userInfo==null){
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(userInfo,user);
        return user;
    }

    /**
     * Pojo转换为Dto
     * @param userInfo Pojo
     * @return
     */
    public static UserDto pojiToDto(User userInfo){
        if(userInfo==null){
            return null;
        }
        UserDto user = new UserDto();
        BeanUtils.copyProperties(userInfo,user);
        return user;
    }
}
