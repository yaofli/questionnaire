<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户账号激活</title>
</head>
<body>
    <c:if test="${error == null}">
        激活成功, 即将跳转到<a href="<c:url value="/"/>" >首页</a>
    </c:if>
    <c:if test="${error != null}">
        验证码错误, 可能用于已经激活, 或者验证码过期, 即将跳转到<a href="<c:url value="/"/>" >首页</a>
    </c:if>
    <script>
        window.setTimeout(function(){
            window.location = "<c:url value="/" />";
        }, 10000);
    </script>
</body>
</html>
