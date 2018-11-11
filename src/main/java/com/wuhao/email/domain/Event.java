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
 * @since 2018-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 事件触发的模块
     */
    private String eventModel;

    /**
     * 事件触发者
     */
    private Integer uid;

    /**
     * 事件触发的时间
     */
    private LocalDateTime eventTime;

    private String eventType;

    /**
     * 事件触发的内容
     */
    private String eventData;

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
