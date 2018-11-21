package com.wuhao.email.controller;


import com.wuhao.email.domain.ApiMode;
import com.wuhao.email.domain.LoginMode;
import com.wuhao.email.domain.User;
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


}
