package com.wuhao.email.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuhao.email.domain.Verify;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
public interface VerifyMapper extends BaseMapper<Verify> {
    @Select("SELECT * FROM verify WHERE uid=#{userId}")
    Verify findVerifyByUserId(int userId);
}
