package com.rongt.controller;

import com.rongt.dto.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 跨域接口
 * @Author rongtao
 * @Data 2019/4/24 13:27
 */
@RestController
public class UserController {

    /**
    * @Description 模拟登录，保存session
    * @param request
    * @Return java.util.Map<java.lang.Object,java.lang.Object>
    * @Author rongtao
    * @Date 2019/4/24 13:28
    */
    @RequestMapping("/login")
    public Map<Object, Object> cros(HttpServletRequest request){
        Map<Object,Object> map = new HashMap<>();
        User user = new User("liyi","love");
        map.put("user",user);
        request.getSession().setAttribute("user", user);
        return map;
    }

    /**
    * @Description 获取用户cookie
    * @param request
    * @Return java.util.Map<java.lang.Object,java.lang.Object>
    * @Author rongtao
    * @Date 2019/4/24 13:29
    */
    @RequestMapping("/cookie")
    public Map<Object, Object> getCookie(HttpServletRequest request){
        Map<Object, Object> map = new HashMap<>();
        HttpSession session = request.getSession();
        System.out.println("session : " + session.getId() + ", user : " + session.getAttribute("user"));

        Cookie[] cookies = request.getCookies();
        System.out.print("cookie : ");
        //jdk1.8；map:逐个处理集合中元素；forEach:遍历
        Arrays.stream(cookies).map(Cookie::getValue).forEach(System.out::println);
        map.put("cookie",cookies);
        return map;
    }

}
