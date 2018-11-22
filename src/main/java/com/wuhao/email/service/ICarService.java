package com.wuhao.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.email.domain.Car;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-19
 */
public interface ICarService extends IService<Car> {
    boolean addCar(int userId,int productId,int productNum);

    List<Car> findCarByUserId(int userId);

    Integer findCarCount(int userId);

    boolean clearCar(int productId,int userId);
}
