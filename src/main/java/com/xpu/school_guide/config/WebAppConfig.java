package com.xpu.school_guide.config;

import com.xpu.school_guide.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
拦截器配置类
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

    // 多个拦截器组成一个拦截器链
    // addPathPatterns 用于添加拦截规则
    // excludePathPatterns 用户排除拦截
    private LoginInterceptor loginInterceptor;

    @Autowired
    public WebAppConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)//添加拦截器
                .addPathPatterns("/**") //拦截所有请求
                .excludePathPatterns("/getCode","/login","/getTasks","/addTasks",
                        "/deleteTaskById","/showSites","/addPlace","/deletePlace","/getTaskById","/finishGroup");//对应的不拦截的请求
    }
}