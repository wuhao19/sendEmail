package com.wuhao.email.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2018-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户地址主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer uid;

    /**
     * 用户地址
     */
    private String address;
    /**
     * 删除地址 0未删除1已删除 默认0
     */
    private Integer addressStatus;

    /**
     * 地址是否为默认 0是普通地址 1是默认地址 默认0
     */
    private Integer addressType;

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
    private Integer creatBy;

    /**
     * 更新人
     */
    private Integer updatBy;


}
