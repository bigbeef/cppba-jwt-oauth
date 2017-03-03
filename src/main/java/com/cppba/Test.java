package com.cppba;


import com.cppba.core.util.JwtUtil;
import com.cppba.core.bean.UserJwt;

public class Test {
    public static void main(String[] args) throws Exception {
        UserJwt userJwt = new UserJwt();
        userJwt.setId(new Long(123));
        userJwt.setUserName("jack");

        String jwt = JwtUtil.createJwt(userJwt);
        System.out.println(jwt);

        UserJwt userJwt1 = JwtUtil.decodeJwt(jwt);
        System.out.println(userJwt1.toString());
    }
}
