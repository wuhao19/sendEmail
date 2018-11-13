package com.wuhao.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.Stock;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-12
 */
public interface IStockService extends IService<Stock> {
    boolean addStockBy(Stock stock);
}
