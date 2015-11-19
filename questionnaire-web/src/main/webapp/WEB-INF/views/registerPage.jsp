<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
%>
<!DOCTYPE html>
<html>
<head>
    <title>BK调查网 - 用户注册</title>
    <meta charset="utf-8"/>

    <link rel="stylesheet" type="text/css" href="<c:url value="/style/main.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/style/register.css"/>" />

    <script src="<c:url value="/js/jquery.js" />" ></script>
    <script src="<c:url value="/js/layer/layer.js"/>" ></script>
    <script src="<c:url value="/js/captcha.js" />" ></script>
    <script src="<c:url value="/js/register.js" />" ></script>


</head>
<body>

<form id="user" action="<c:url value="/user/register" />" method="post">
    <legend>用户注册</legend>
    <ul>
        <li>
            <label for="name">用户名:</label>
            <input id="name" name="name" type="text" />
        </li>
        <li>
            <label for="account">手机号/邮箱: </label>
            <input id="account" name="account" type="text" />
        </li>
        <li>
            <label for="password">密码:</label>
            <input id="password" name="password" type="password" />
        </li>
        <li>
            <label for="confirmPassword" title="请输入确认密码">确认密码: </label>
            <input id="confirmPassword" type="password" name="confirmPassword">
        </li>
        <li>
            <label for="imageCaptcha">验证码: </label>
            <input id="imageCaptcha" type="text" name="__answer__" />
            <a id="flushCaptcha" href="#"><img src="<c:url value="/captcha.png"/>" /></a>
        </li>
        <li>
            <label for="smsCaptcha">手机验证码: </label>
            <input id="smsCaptcha" type="text" name="__smsCaptcha__" />
            <input id="getSmsCaptcha" type="button" value="获取手机验证码" />
        </li>
        <li>
            <input id="submit" type="submit" value="注册"/>
        </li>
    </ul>
</form>
</body>
</html>
