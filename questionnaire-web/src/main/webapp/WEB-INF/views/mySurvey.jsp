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
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>我的问卷</title>
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
    <script src="js/layer/layer.js"></script>
    <script src="js/mySurvey.js"></script>
</head>
<body>
<jsp:include page="templet/header.jsp" />
<div class="am-margin-lg">
    <a href="addSurveyPage" class="am-btn am-btn-primary">创建新问卷</a>

    <div id="surveys" class="am-panel-group am-margin-vertical-sm">
        <c:forEach items="${surveyList}" var="survey">
            <section class="am-panel am-panel-default">
                <header class="am-panel-hd am-cf">
                    <h3 class="am-fl am-panel-title am-margin-right-sm am-u-sm-8">
                        <c:out value="${survey.title}" escapeXml="true"/>
                    </h3>
                    <a href="#" class="am-fl am-margin-horizontal-sm reverseDesigning">
                        ${survey.designing ? "开放问卷" : "关闭问卷"}
                    </a>
                    <a class="am-margin-horizontal-sm">修改</a>
                    <a href="#" class="am-margin-horizontal-sm deleteSurvey">删除</a>
                    <input type="hidden" value="${survey.id}"/>
                </header>
                <div class="am-panel-bd">
                    <span class="am-u-sm-3">创建时间:
                        <fmt:formatDate value="${survey.createTime}" pattern="yyyy-MM-dd HH:mm" />
                    </span>
                    <span class="am-u-sm-3">修改时间:
                        <fmt:formatDate value="${survey.modifyTime}" pattern="yyyy-MM-dd HH:mm" />
                    </span>
                    <span data-am-collapse="{parent: '#surveys', target: '#survey_${survey.id}'}">
                        说明<span class="am-icon-caret-down"></span>
                    </span>
                </div>
                <div id="survey_${survey.id}" class="am-panel-collapse am-collapse">
                    <div class="am-panel-bd">
                       <c:out value="${survey.detailDescribe}" escapeXml="true"/>
                    </div>
                </div>
            </section>
        </c:forEach>
    </div>
    <jsp:include page="templet/footer.jsp"/>
</body>
</html>
