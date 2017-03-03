package com.cppba.web;

import com.cppba.core.annotation.RequiresRoles;
import com.cppba.core.bean.UserJwt;
import com.cppba.core.util.CommonUtil;
import com.cppba.core.util.JwtUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Controller
public class TestController {

    @RequestMapping("/index")
    @ResponseBody
    @RequiresRoles({"admin","user"})
    public Object index(){
        UserJwt userJwt = new UserJwt();
        userJwt.setId(new Long(123));
        userJwt.setUserName("jack");
        return userJwt;
    }

    @RequestMapping("/login")
    @ResponseBody
    public Object login(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value="userName", defaultValue="")String userName,
            @RequestParam(value="password", defaultValue="")String password
    ) throws Exception {
        Map map = new HashMap<String,Object>();

        //登录成功
        UserJwt userJwt = new UserJwt();
        userJwt.setId(new Long(123));
        userJwt.setUserName("jack");
        userJwt.setRoles(new String[]{"admin"});
        String token = JwtUtil.createJwt(userJwt);

        CommonUtil.setCookie("token",token,0,response);

        map.put("token",token);

        return map;
    }
}
