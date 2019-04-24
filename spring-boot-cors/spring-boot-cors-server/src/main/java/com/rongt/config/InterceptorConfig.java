package com.rongt.config;

import com.rongt.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description WebMvcConfigurerAdapter:扩展mvc
 * @Author rongtao
 * @Data 2019/4/24 13:41
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册登录超时拦截器，并排除拦截登录请求
        registry.addInterceptor(new SessionInterceptor()).excludePathPatterns("/**/login");
        super.addInterceptors(registry);
    }
}
