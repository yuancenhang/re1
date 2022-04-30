<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + request.getContextPath() + "/"; %>
<!--_________________________https__________________________localhost______________________8080_______________________crm-->
<html>
<head>
    <meta charset="UTF-8">
    <title>登陆页面</title>
    <script type="text/javascript" src="jquery-3.6.0.js"></script>
    <base href=<%=basePath%>>

</head>
<body>
    <script type="text/javascript" >

    </script>
    <h1>这里是登陆页面</h1>
    <div style="background-image:url('WEB-INF/img/login.png')" >div</div>
    <form action="">
        <input type="text" id="username">
        <input type="password" id="password">
        <input type="button" value="登陆" id="btnDl">
    </form>
</body>
</html>