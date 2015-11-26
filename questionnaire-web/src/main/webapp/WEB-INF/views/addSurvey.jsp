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

<%--
  User: 李冰凯
  Date: 2015/11/25
  Time: 12:45
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="l" uri="/WEB-INF/lbktags.tld"%>
<!doctype html>
<html class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>创建空白问卷</title>
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
</head>
<body>
<jsp:include page="templet/header.jsp" />

<form action="/survey" class="am-form am-form-horizontal" method="post">
    <div class="am-form-group">
        <label for="title" class="am-u-sm-push-2 am-u-sm-2 am-form-label">问卷标题</label>

        <div class="am-u-sm-6 am-u-sm-pull-2">
            <input type="text" id="title" placeholder="问卷标题" name="title" required="required" />
        </div>
    </div>

    <div class="am-form-group">
        <span class="am-u-sm-push-2 am-u-sm-2 am-form-label">问卷说明(可选)</span>

        <div class="am-u-sm-6  am-u-sm-pull-2">
            <textarea name="detailDescribe" cols="30" rows="10" maxlength="500"></textarea>
        </div>
    </div>
    <l:token/>
    <div class="am-form-group">
        <div class="am-u-sm-6 am-u-sm-push-6">
            <button type="submit" class="am-btn am-btn-primary">确定</button>
        </div>
    </div>
</form>

<jsp:include page="templet/footer.jsp" />
</body>
</html>
