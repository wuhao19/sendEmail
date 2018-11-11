package com.wuhao.email.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Event;
import com.wuhao.email.mapper.EventMapper;
import com.wuhao.email.service.IEventService;
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
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {
    @Autowired
    private EventMapper eventMapper;
    /**
     * 保存事件
     * @param event 事件
     * @return
     */
    @Override
    public boolean saveEvent(Event event) {
        int result = eventMapper.insert(event);
        if (result!=1){
            return false;
        }else {
            return true;
        }
    }
}
