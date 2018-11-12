package com.wuhao.email.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.email.domain.Verify;
import com.wuhao.email.mapper.VerifyMapper;
import com.wuhao.email.service.IVerifyService;
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
public class VerifyServiceImpl extends ServiceImpl<VerifyMapper, Verify> implements IVerifyService {
    @Autowired
    private VerifyMapper verifyMapper;

    @Override
    public boolean saveVerifyCode(Verify verify) {
        if (verify==null){
            return false;
        }
        int result = verifyMapper.insert(verify);
        if(result!=1){
            return false;
        }
        return true;
    }

    @Override
    public Verify findVerifyByUserId(int userId) {
        Verify verify= verifyMapper.findVerifyByUserId(userId);
        if(verify == null){
            return null;
        }
        return verify;
    }
}
