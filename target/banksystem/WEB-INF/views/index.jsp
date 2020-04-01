<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>CA认证中心 | 证书申请|证书查询|API|证书下载|证书撤销|CA公钥|CA简介</title>
</head>

<body>
<center>
    <form action="findUser" method="post">
        请输入用户ID:<input type="text" name="id">
        <input type="submit" value="确定">
    </form>
</center>
<h1>name : ${name}</h1>
</body>
</html>

