<%--
  ~ Copyright 2015 LBK
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>BK调查网</title>
    <base href="<c:url value="/"/>">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="icon" type="image/png" href="assets/i/favicon.png" />

    <meta name="mobile-web-app-capable" content="yes">
    <link rel="icon" sizes="192x192" href="assets/i/app-icon72x72@2x.png" />

    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-title" content="Amaze UI" />
    <link rel="apple-touch-icon-precomposed" href="assets/i/app-icon72x72@2x.png" />

    <meta name="msapplication-TileImage" content="assets/i/app-icon72x72@2x.png" />
    <meta name="msapplication-TileColor" content="#0e90d2">

    <link rel="stylesheet" href="assets/css/amazeui.min.css"/>
    <link rel="stylesheet" href="assets/css/app.css" />

    <title>激活XX账号</title>
    <script src="js/jquery.min.js"></script>
    <script src="js/layer/layer.js"></script>
    <script>
        $(function (){
            $('#resendEmail').click(function (){
                var data = {email: '${param.email}'};
                $.get('/user/registerEmailSend', data, function (data){
                    if( data == 'success' ){
                        layer.msg('发送成功');
                    }
                    else if( data == 'invalid email' ){
                        layer.msg('邮箱无效, 请重新注册');
                    }
                    else{
                        // account error
                        layer.msg('发送失败');
                    }
                });
                return false;
            });
        });
    </script>
</head>
<body>
<jsp:include page="templet/header.jsp" />

<div>
    <p class="am-text-center">我们已经向您的邮箱${param.email}发送了一封激活邮件，请点击邮件中的链接完成注册！</p>
    <p class="am-text-center"><a href="${emailUrl }" target="_blank" class="am-btn am-btn-default">立即进入邮箱</a></p>

    <p class="am-text-center">没有收到邮件? 点击<a id="resendEmail" href="#">重新发送</a></p>
</div>

<jsp:include page="templet/footer.jsp" />

</body>
</html>
