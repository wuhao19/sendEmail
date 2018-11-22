package com.wuhao.email.controller;


import com.wuhao.email.domain.Address;
import com.wuhao.email.domain.ApiMode;
import com.wuhao.email.domain.LoginMode;
import com.wuhao.email.domain.User;
import com.wuhao.email.service.IAddressService;
import com.wuhao.email.service.IUserService;
import com.wuhao.email.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wuhao
 * @since 2018-11-11
 */
@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    private IUserService userService;
    @Autowired
    private IAddressService addressService;

    /**
     * 用户的登录操作
     * @param userName
     * @param password
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/doLogin")
    public ApiMode doLogin(@RequestParam(value = "userName") String userName,
                           @RequestParam(value = "password")String password,
                           HttpServletRequest request){
        if (!UserUtils.checkScanInfo(userName,password)) {
            return new ApiMode("你输入的信息不合法");
        }
        LoginMode loginMode = UserUtils.loginType(userName);
        User user = userService.doLogin(userName, password, loginMode);
        if (user==null){
            return new ApiMode("用户名或者密码错误");
        }
        request.getSession().setAttribute("user",user);
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        ApiMode apiMode = new ApiMode();
        apiMode.setDate(map);
        return apiMode;
    }

    /**
     * 检查是否有用户登录
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/checkUserLogin")
    public ApiMode checkUserLogin(HttpServletRequest request){
        User user =(User) request.getSession().getAttribute("user");
        if (user==null){
            return new ApiMode("还没有用户登录");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        ApiMode apiMode = new ApiMode();
        apiMode.setDate(map);
        return apiMode;
    }

    /**
     * 查询用户的信息
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/findUser")
    public ApiMode findUser(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return new ApiMode("还没有用户登录");
        }
        ApiMode apiMode;
        User userById = userService.findUserById(user.getUserId());
        if(userById==null){
            return new ApiMode("未找到用户信息");
        }
        List<Address> addressList = addressService.findAddressByUserId(userById.getUserId());
        if (addressList==null||addressList.size()==0){
            apiMode= new ApiMode("该用户还没有地址");
            apiMode.setCode(2);//没有地址
            Map<String,Object> map = new HashMap<>();
            map.put("user",userById);
            apiMode.setDate(map);
            return apiMode;
        }else {
            Map<String,Object> map = new HashMap<>();
            map.put("user",userById);
            map.put("addressList",addressList);
            apiMode= new ApiMode();
            apiMode.setDate(map);
            return apiMode;
        }
    }
}
