package com.wuhao.email.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Stock;
import com.wuhao.email.mapper.StockMapper;
import com.wuhao.email.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-12
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {
    @Autowired
    private StockMapper stockMapper;
    @Override
    public boolean addStockBy(Stock stock) {
        if (stock==null){
            return false;
        }
        int result = stockMapper.insert(stock);
        if (result!=1){
            return false;
        }
        return true;
    }
}
