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
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <link rel="icon" type="image/png" href="<c:url value="/assets/i/favicon.png"/>" />

    <meta name="mobile-web-app-capable" content="yes">
    <link rel="icon" sizes="192x192" href="<c:url value="/assets/i/app-icon72x72@2x.png"/>" />

    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-title" content="Amaze UI" />
    <link rel="apple-touch-icon-precomposed" href="<c:url value="/assets/i/app-icon72x72@2x.png"/>" />

    <meta name="msapplication-TileImage" content="<c:url value="/assets/i/app-icon72x72@2x.png"/>" />
    <meta name="msapplication-TileColor" content="#0e90d2">

    <link rel="stylesheet" href="<c:url value="/assets/css/amazeui.min.css"/>" />
    <link rel="stylesheet" href="<c:url value="/assets/css/app.css"/>" />

    <script src="<c:url value="/js/jquery.min.js"/>" ></script>
    <script src="<c:url value="/js/layer/layer.js"/>" ></script>
</head>
<body>

<jsp:include page="templet/header.jsp" />

<jsp:include page="templet/footer.jsp"/>

</body>
</html>