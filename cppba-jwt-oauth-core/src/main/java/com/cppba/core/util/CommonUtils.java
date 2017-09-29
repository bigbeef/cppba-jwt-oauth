package com.cppba.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonUtils {

	//保存coockie
	public static void setCookie(String name,String value,int maxAge,HttpServletResponse response){
		Cookie cookie = new Cookie(name, value);
		if(maxAge > 0){
			cookie.setMaxAge(maxAge);
		}
		response.addCookie(cookie);
	}

	//获取coockie
	public static String getCookie(String name,HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		for(Cookie c :cookies ){
			if(c.getName().equals(name)){
				return c.getValue();
			}
		}
		return null;
	}
}
