package com.rongt.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description 跨域请求拦截器
 * @Author rongtao
 * @Data 2019/4/24 13:32
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    public CorsFilter(){
        System.out.println("CorsFilter Constructor...");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CorsFilter init...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("CorsFilter doFilter...");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //允许请求携带认证信息(cookie)
        res.setHeader("Access-Control-Allow-Credentials", "true");
        //指定允许其他域名访问
        res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        //允许请求的类型
        res.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        //允许的请求头字段
        res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        //设置预检请求的有效期
        //浏览器同源策略：出于安全考虑，浏览器限制跨域的http请求。怎样限制呢？通过发送两次请求：预检请求、用户请求。
        //1、预检请求作用：获知服务器是否允许该跨域请求：如果允许，才发起第二次真实的请求；如果不允许，则拦截第二次请求
        //2、单位:s,在此期间不用发送预检请求。
        //3、若为0：表示每次请求都发送预检请求,每个ajax请求之前都会先发送预检请求。
        res.setHeader("Access-Control-Max-Age", "3600");
        //OPTIONS Method表示浏览器发送的预检请求。
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        System.out.println("CorsFilter destroy...");
    }
}
