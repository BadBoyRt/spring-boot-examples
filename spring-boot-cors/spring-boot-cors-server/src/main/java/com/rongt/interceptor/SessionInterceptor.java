package com.rongt.interceptor;

import com.rongt.dto.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Description 登录超时拦截器
 * @Author rongtao
 * @Data 2019/4/24 13:37
 */
@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {
    //拦截action
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user);
        //session中User过期
        if(user == null){
            String uri = request.getRequestURI();
            System.out.println(uri);
            //ajax请求响应头会有，x-requested-with
            if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with")
                    .equalsIgnoreCase("XMLHttpRequest")) {
                //在响应头设置session状态
                response.setHeader("sessionstatus", "timeout");
                response.setHeader("url", uri.substring(0, uri.indexOf("/", 1)));
            } else {
                PrintWriter out = response.getWriter();
                StringBuilder sb = new StringBuilder();
                sb.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
                sb.append("alert(\"登录超时，请重新登录\");");
                sb.append("window.top.location.href=\"");
                sb.append("/login.jsp");
                sb.append("\";</script>");
                out.print(sb.toString());
                out.close();
            }
            //返回false不再调用其他拦截器和处理器
            return false;
        }
        return true;
    }
}
