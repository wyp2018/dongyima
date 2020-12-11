<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>cas测试1</title>
</head>
<body>

欢迎来到cas认证系统


<%--为获取远程登录名--%>
<%=request.getRemoteUser()%>

<a href="http://localhost:8080/cas/logout?service=http://www.baidu.com">退出登录</a>

</body>
</html>
