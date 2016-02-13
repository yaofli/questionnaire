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

<!doctype html>
<html class="no-js">
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

    <link rel="stylesheet" href="assets/css/amazeui.min.css" />
    <link rel="stylesheet" href="assets/css/app.css" />

    <script src="js/jquery.min.js"></script>
    <script src="js/layer/layer.js" ></script>
    <script src="js/register.js" ></script>
    <script src="js/captcha.js" ></script>
</head>
<body>
<jsp:include page="templet/header.jsp" />

<div class="am-u-sm-12">
    <div class="am-u-md-7 am-u-md-push-2">
        <form id="userRegister" action="#" class="am-form am-form-horizontal" method="post">
            <div class="am-form-group">
                <label for="name" class="am-u-sm-3 am-form-label">用户名</label>
                <div class="am-u-sm-9">
                    <input type="text" id="name" placeholder="用户名" name="name" required="required" />
                </div>
            </div>

            <div class="am-form-group">
                <label for="account" class="am-u-sm-3 am-form-label">手机号 / 邮箱</label>

                <div class="am-u-sm-9">
                    <input id="account" name="account" type="text" placeholder="手机号 / 邮箱" required="required" />
                </div>
            </div>

            <div class="am-form-group">
                <label for="password" class="am-u-sm-3 am-form-label">密码</label>
                <div class="am-u-sm-9">
                    <input type="password" id="password" name="password" placeholder="密码" required="required" />
                </div>
            </div>

            <div class="am-form-group">
                <label for="confirmPassword" class="am-u-sm-3 am-form-label">确认密码</label>

                <div class="am-u-sm-9">
                    <input type="password" id="confirmPassword" placeholder="确认密码" required="required" />
                </div>
            </div>

            <div class="am-form-group">
                <label for="imageCaptcha" class="am-u-sm-3 am-form-label">验证码</label>

                <div class="am-u-sm-6">
                    <input class="am-u-md-8" type="text" id="imageCaptcha" name="__answer__" placeholder="验证码" />
                </div>
                <div class="am-u-sm-3">
                    <a id="flushCaptcha" class="am-u-md-4" href="#"><img src="captcha.png"/></a>
                </div>
            </div>

            <div id="smsModule" class="am-form-group am-hide">
                <label for="smsCaptcha" class="am-u-sm-3 am-form-label">手机验证码</label>

                <div class="am-u-sm-6">
                    <input type="number" name="__smsCaptcha__" id="smsCaptcha" placeholder="手机验证码" />
                </div>
                <div class="am-u-sm-3">
                    <input type="button" class="am-btn" id="getSmsCaptcha" value="获取手机验证码"/>
                </div>
            </div>

            <div class="am-form-group">
                <div class="am-u-sm-6 am-u-sm-push-6">
                    <button type="submit" class="am-btn am-btn-primary">注册</button>
                </div>
            </div>
        </form>
    </div>
</div>

<jsp:include page="templet/footer.jsp" />

</body>
</html>