<%--
  Created by IntelliJ IDEA.
  User: 浴缸
  Date: 2018/2/8
  Time: 17:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
</head>
<body>
<h2>登录界面</h2>
<form name="form1" action="/backManager/user/login.do" method="post" enctype="multipart/form-data">
    username:<input type="text" name="username"/>
    password:<input type="password" name="password"/>
    <input type="submit" value="login"/>
</form>
</body>
</html>
