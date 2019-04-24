<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
<!-- <a id="cros" onclick="go()">cros - session</a> -->

<button id="login" onclick="login()">login</button><br/>
<button id="cros" onclick="cros()">cros</button><br/>

<script src="/cinema/js/jquery-1.8.0.min.js" type="text/javascript"></script>
<script>
    function login(){
        $.ajax({
            url:"http://127.0.0.1:8080/cors-server/login",
            type:"post",
            dataType:"json",
            xhrFields: {//携带cookie信息
                withCredentials: true
            },
            success:function(json){
                console.log(json);
            }
        });
    }
    function cros(){
        $.ajax({
            url:"http://127.0.0.1:8080/cors-server/cookie",
            type:"post",
            dataType:"json",
            xhrFields: {
                withCredentials: true
            },
            success:function(json){
                console.log(json);
            }
        });
    }
</script>
</body>