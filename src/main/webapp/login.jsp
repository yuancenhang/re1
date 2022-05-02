<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + request.getContextPath() + "/"; %>
<!--_________________________https__________________________localhost______________________8080_______________________crm-->
<html>
<head>
    <base href=<%=basePath%>>
    <meta charset="UTF-8">
    <title>登陆页面</title>
    <script type="text/javascript" src="jquery-3.6.0.js"></script>

    <style type="text/css">
        #beijing{
            background-image: url("img/login.png");
            background-size: cover;
            background-repeat: no-repeat;
        }
        #btnDl{
            background-color: aquamarine;
            border: cornflowerblue;
            width: 178px;

        }
    </style>
</head>
<body id="beijing">
    <script type="text/javascript" >

        $(function (){
            //在任何时候，都把登陆页作为顶层窗口
            if (window.top!=window){
                window.top.location = window.location;
            }
            var user = $("#username");
            var pass = $("#password");
            //清空输入框
            user.val("");
            pass.val("");

            //加载完成让输入框获取焦点
            user.focus();

            //登陆的ajax请求
            $("#btnDl").on("click",function (){
                login();
            })

            //按回车键登陆
            pass.onkeydown=function (event) {
                if(event.keyCode === 13){
                    login();
                }
            }
        })
        //封装的登陆方法
        function login() {
            var u = $.trim($("#username").val());
            var p = $.trim($("#password").val());
            var e = $("#err");
            if ("" === u || "" ===p){
                e.html("账号密码不能为空");
                return false;
            }
            $.ajax({
                url : "User/login.sv",
                data : {
                    "username" : u,
                    "password" : p
                },
                dataType : "json",
                success : function (data){
                    //{ok:false,msg:无法登陆的原因}
                    if (data.ok === false){
                        e.html(data.msg);
                        return false;
                    }
                    //执行到这里说明没问题，跳转页面
                    window.location.href = "work/index.html";
                },
                type : "post"
            })
        }
    </script>

    <center>
        <div style="padding-top: 250px">
            <div>
                <h1>欢迎来到CRM登陆页面</h1>
            </div>
            <br/>
            <div style="width: max-content;border: 2px solid dodgerblue;padding: 100px">
                <div>
                    <label for="username"></label><input type="text" id="username" placeholder="用户名"><br/><br/>
                </div>
                <div>
                    <label for="password"></label><input type="password" id="password" placeholder="密码"><br/>
                </div>
                <div>
                    <span id="err" style="display:block ;color: red;min-height: 40px;margin-top: 10px"></span>
                </div>
                <div>
                    <button type="button" id="btnDl">登陆</button>
                </div>
            </div>
        </div>
    </center>

</body>
</html>