package com.wuhao.email.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户的手机号
     */
    private String userPhone;

    /**
     * 邮箱的验证状态 1是未验证 0已经验证
     */
    private Integer emailStatus = 1;

    /**
     * 手机的验证状态 1是未验证 0已经验证
     */
    private Integer phoneStatus = 1;

    /**
     * 用户的头像
     */
    private String userIcon;

    /**
     * 创建时间
     */
    private LocalDateTime crateTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Integer crateBy;

    /**
     * 更新人
     */
    private Integer updateBy;


}
