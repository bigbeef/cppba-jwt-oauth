package com.cppba.web;

import com.cppba.core.annotation.RequiresRoles;
import com.cppba.core.bean.JwtUser;
import com.cppba.core.util.CommonUtils;
import com.cppba.core.util.JwtUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
public class TestController {

    @RequestMapping("/index")
    @RequiresRoles({"admin","user"})
    public Object index(){
        JwtUser userJwt = new JwtUser();
        userJwt.setId(new Long(123));
        userJwt.setUserName("jack");
        return userJwt;
    }

    @RequestMapping("/login")
    public Object login(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value="userName", defaultValue="")String userName,
            @RequestParam(value="password", defaultValue="")String password
    ) throws Exception {
        Map map = new HashMap<String,Object>();

        //登录成功
        JwtUser userJwt = new JwtUser();
        userJwt.setId(new Long(123));
        userJwt.setUserName("jack");
        userJwt.setRoles(new String[]{"admin"});
        String token = JwtUtils.createJwt(userJwt);

        CommonUtils.setCookie("token",token,0,response);

        map.put("token",token);

        return map;
    }
}
