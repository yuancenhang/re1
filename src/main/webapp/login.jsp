<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + request.getContextPath() + "/"; %>
<!--_________________________https__________________________localhost______________________8080_______________________crm-->
<html>
<head>
    <meta charset="UTF-8">
    <title>登陆页面</title>
    <script type="text/javascript" src="jquery-3.6.0.js"></script>
    <base href=<%=basePath%>>
    <style type="text/css">
        #beijing{
            background-image: url("img/login.png");
            width: 100%;
            height: 671px;
        }
        #btnDl{
            background-color: aquamarine;
            border: black;
            width: 10%;
        }
    </style>
</head>
<body>
    <script type="text/javascript" >
        $(function (){
            //加载完成让输入框获取焦点
            $("username").onblur;
            //登陆的ajax请求
            $("btnDl").on("click",function (){
                $.ajax({
                    url : "User/login",
                    data : {
                        "username" : $("username").val(),
                        "password" : $("password").val()
                    },
                    dataType : "json",
                    success : function (data){
                        //{ok:false,msg:无法登陆的原因}
                        if (data.ok === false){
                            $(err).innerHTML = data.msg;
                            return false;
                        }
                        //执行到这里说明没问题，跳转页面
                        window.location.href = "in/tishi.html";
                    },
                    type : "post"
                })
            })
        })
    </script>

    <div id="beijing">
        <center>
            <h1>欢迎来到CRM登陆页面</h1>
            <span style="background-color: burlywood">
                <form>
                    <label for="username">账号</label><input type="text" id="username"><br/><br/>
                    <label for="password">密码</label><input type="password" id="password"><br/>
                    <div id="err" style="color: red"></div>
                    <input type="button" value="登陆" id="btnDl">
                </form>
            </span>
        </center>
    </div>

</body>
</html>