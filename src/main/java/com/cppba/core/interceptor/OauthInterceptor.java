package com.cppba.core.interceptor;


import com.cppba.core.util.CommonUtil;
import com.cppba.core.util.JwtUtil;
import com.cppba.core.annotation.RequiresRoles;
import com.cppba.core.bean.UserJwt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class OauthInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //该controller的角色权限注解
        RequiresRoles annotation = method.getAnnotation(RequiresRoles.class);
        //如果不需要权限验证的controller
        if(annotation == null){
            return true;
        }

        //获取客户的传来的token,此处可以根据前后端约定自定义怎么获取token
        String token = CommonUtil.getCookie("token", request);
        System.out.println("token:"+token);

        //没有token
        if(StringUtils.isBlank(token)){
            toUnauthorized(response);
            return false;
        }

        //验证token签名是否合法
        boolean verify = JwtUtil.verify(token);
        System.out.println("verify:"+verify);
        if(!verify){
            toUnauthorized(response);
            return false;
        }

        //解码用户信息
        UserJwt userJwt = JwtUtil.decodeJwt(token);
        System.out.println("userJwt:"+userJwt.toString());
        String[] roles = userJwt.getRoles();
        List<String> rolesList = Arrays.asList(roles);

        //是否包含角色权限注解
        if(annotation!=null){
            String[] needRoles = annotation.value();
            List<String> needRoleList = Arrays.asList(needRoles);
            System.out.println("needRoles:"+ needRoleList.toString());
            //需要的权限该用户是否都包含
            for(String needRole : needRoleList){
                if(StringUtils.isBlank(needRole)){
                    continue;
                }
                if(!rolesList.contains(needRole)){
                    toUnauthorized(response);
                   return false;
                }
            }
        }

        String newToken = JwtUtil.newToken(token);
        if(!StringUtils.equals(token,newToken)){
            CommonUtil.setCookie("token",newToken,0,response);
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    //返回未授权结果
    private void toUnauthorized(HttpServletResponse response){
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
