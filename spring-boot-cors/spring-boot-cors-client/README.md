# 客户端
- ajax调用服务端接口
```
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
```
