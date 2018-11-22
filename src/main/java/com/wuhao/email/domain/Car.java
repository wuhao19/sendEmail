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
 * @since 2018-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户的id
     */
    private Integer uid;

    /**
     * 购物车的状态 0未删除1已删除
     */
    private Integer carStatus;

    /**
     * 商品id
     */
    private Integer pid;

    /**
     * 商品对应的数量
     */
    private Integer pNum;

    /**
     * 创建时间
     */
    private LocalDateTime creatTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建的人id
     */
    private Integer creatBy;

    /**
     * 更新的人id
     */
    private Integer updateBy;


}
