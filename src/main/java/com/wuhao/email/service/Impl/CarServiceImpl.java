package com.wuhao.email.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Car;
import com.wuhao.email.excptionHandler.MyException;
import com.wuhao.email.mapper.CarMapper;
import com.wuhao.email.service.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wuhao
 * @since 2018-11-19
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {
    @Autowired
    private CarMapper carMapper;
    @Override
    public boolean addCar(int userId, int productId,int productNum) {
        if (userId==0||productId==0){
            return false;
        }
        //查询car中是否用相同用户的相同商品，否则进行更新
        Car car1 = carMapper.selectOne(new QueryWrapper<Car>().eq("uid", userId).eq("pid", productId));
        if (car1!=null){
            //进行更新操作
          productNum = productNum + car1.getPNum();
          car1.setPNum(productNum);
            int i = carMapper.updateById(car1);
            if (i==0){
                throw new MyException(123,"商品添加购物车失败啦");
            }
            return true;
        }
        //进行插入操作
        Car car = new Car();
        car.setPNum(productNum);
        car.setUid(userId);
        car.setPid(productId);
        car.setCreatBy(userId);
        car.setUpdateBy(userId);
        int insert = carMapper.insert(car);
        if (insert==0){
            return false;
        }
        return true;
    }

    @Override
    public List<Car> findCarByUserId(int userId) {
        if (userId==0){
            return null;
        }
        List<Car> carList = carMapper.selectList(new QueryWrapper<Car>().eq("uid", userId));
        if (carList==null){
            throw new MyException(123,"您的购物车内没有商品");
        }
        return carList;
    }

    @Override
    public Integer findCarCount(int userId) {
       return carMapper.selectCount(new QueryWrapper<Car>().eq("uid", userId));
    }
}
