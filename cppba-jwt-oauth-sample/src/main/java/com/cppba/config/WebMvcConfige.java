package com.cppba.config;

import com.cppba.core.interceptor.OauthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author winfed
 * @create 2017-09-29 18:19
 */
@Configuration
public class WebMvcConfige extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OauthInterceptor()).addPathPatterns("/**");
    }
}
