package com.wuhao.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.Event;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
public interface IEventService extends IService<Event> {
    /**
     * 保存事件
     * @param event 事件
     * @return
     */
    boolean saveEvent(Event event);

}
