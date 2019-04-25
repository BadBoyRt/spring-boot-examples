# 使用CROS解决跨域及一致性Session问题



## 解决问题

​	前后端分离项目背景下，跨域访问及一致性session问题（是否同一用户）。

​	ps：以前做的项目都是前、后端部署在一个tomcat容器中，不会涉及到跨域访问以及一致性session问题。随着前后端分离架构的流行，前、后端部署在不同服务器等都会涉及到跨域等问题。



## 同源策略

​	同源策略是浏览器保证安全的基础，它的含义是指，A网页设置的 Cookie，B网页不能打开，除非这两个网页同源。 所谓同源必须**同时满足**以下3点：

- 协议相同

- 域名相同

- 端口相同

  例如：www.abc.com:8080/login	协议是http，域名是www.abc.com，端口是8080。



​	当一个资源从与该资源本身所在的服务器**不同的域、协议或端口**请求一个资源时，资源会发起一个**跨域 HTTP 请求**。

​	出于安全考虑，浏览器会限制从脚本发起的跨域HTTP请求。浏览器向请求的服务器发送两次请求：第一次浏览器使用OPTIONS方法发起一个**预检请求**，第二次才是**异步请求**。

​	预检请求查看服务器是否允许跨域请求：如果允许，则发送异步请求；否则拦截用户请求。



## 跨域解决办法

- **JSONP**

  ​	只支持get请求，限制比较大。

- **CROS**

  ​	CORS跨域资源共享是一种机制，它使用额外的 HTTP头来告诉浏览器  让运行在一个 origin上的Web应用被准许访问来自不同源服务器上的指定的资源。



## 解决步骤

解决两个问题：跨域、Session不一致问题。

1. ajax请求中，加入xhrFields:{withCredentials: true}，表示携带cookie信息。
非常重要：保证了前端每次请求时携带的是同一个sessionid
   ```js
   function login(){
   	$.ajax({
   		url:"http://127.0.0.1:8080/cors-server/login",
   		type:"post",
   		dataType:"json",
   		xhrFields: {//携带cookie信息
               withCredentials: true
           },
   		success:function(json){
   			request.sessionScope
   			alert(json);
   		}
   	});
   }
   ```

   ​

2. SpringBoot项目中加入CORS过滤器，为请求附加头信息，使其满足跨域

   ```java
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
   ```

3. 设置session有效期

   ```properties
   server.port=8080
   server.servlet.context-path=/cors-server
   #session超时时间
   server.servlet.session.timeout=1
   ```

4. 设置session超时拦截器

   ```java
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
               return false;
           }
           return true;
       }
   }
   ```
5. SpringBoot框架中向MVC注册session超时拦截器，并设置拦截路径
```java
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册登录超时拦截器，并排除拦截登录请求
        registry.addInterceptor(new SessionInterceptor()).excludePathPatterns("/**/login");
        super.addInterceptors(registry);
    }
}
```

