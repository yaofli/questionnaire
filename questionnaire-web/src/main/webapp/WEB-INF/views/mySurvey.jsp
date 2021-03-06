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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <a href="/addSurveyPage" class="am-btn am-btn-primary">创建新问卷</a>

    <form:form action="/survey/mySurvey" method="get" modelAttribute="condition"
               class="am-form-inline am-margin-vertical">
    <div class="am-form-group">
        <form:input path="title" class="am-form-field" placeholder="问卷名或者id" />
    </div>
    <div class="am-form-group">
        <form:select path="status" class="am-form-field">
            <form:option value="" label="状态" />
            <form:option value="0" label="发布" />
            <form:option value="1" label="设计" />
        </form:select>
    </div>
    每页:
    <form:select path="page.pageSize" class="am-form-field">
        <form:option value="3"/>
        <form:option value="5"/>
        <form:option value="8"/>
        <form:option value="10"/>
    </form:select>
    条
    <input type="submit" value="查询" class="am-btn am-btn-default" />
    </form:form>

    <div id="surveys" class="am-panel-group am-margin-vertical-sm">
        <c:forEach items="${page.content}" var="survey">
            <section class="am-panel am-panel-default">
                <header class="am-panel-hd am-cf">
                    <h3 class="am-fl am-panel-title am-margin-right-sm am-u-sm-6">
                        <c:out value="标题:${survey.title}|id:${survey.id}" escapeXml="true" />
                    </h3>

                    <div class="am-dropdown am-margin-horizontal-sm" data-am-dropdown>
                        <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;">
                            回收问卷<span class="am-icon-caret-down"></span>
                        </a>
                        <ul class="am-dropdown-content">
                            <li><a href="/survey/participate/${survey.id}">问卷链接</a></li>
                        </ul>
                    </div>
                    <div class="am-dropdown am-margin-horizontal-sm" data-am-dropdown>
                        <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;">
                            分析下载<span class="am-icon-caret-down"></span>
                        </a>
                        <ul class="am-dropdown-content">
                            <li><a href="/survey/statistics/${survey.id}">统计&amp;分析</a></li>
                        </ul>
                    </div>
                    <a href="/survey/reverseDesigning/${survey.id}"
                       class="am-margin-horizontal-sm reverseDesigning">
                            ${survey.isDesign() ? "开放问卷" : "关闭问卷"}
                    </a>

                    <div class="am-dropdown am-margin-horizontal-sm" data-am-dropdown>
                        <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;">
                            设计问卷<span class="am-icon-caret-down"></span>
                        </a>
                        <ul class="am-dropdown-content">
                            <li><a href="/survey/design/${survey.id}">修改问卷</a></li>
                            <li><a href="javascript:;">问卷设置</a></li>
                        </ul>
                    </div>
                    <a href="/survey/${survey.id}"
                       class="am-margin-horizontal-sm deleteSurvey">删除</a>
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
                        <c:out value="${survey.detailDescribe}" escapeXml="true" />
                    </div>
                </div>
            </section>
        </c:forEach>
    </div>

    <ul data-am-widget="pagination" class="am-pagination am-center" style="width: ${90*2+page.totalPage*50}px">
        <li class="am-pagination-prev ">
            <a href="/survey/mySurvey?${condition.httpQueryString}&page.pageNo=${page.prevPage}" class="">上一页</a>
        </li>
        <c:forEach items="${page.pageList}" var="pageNo">
            <li class="${page.pageNo==pageNo ? "am-active" : ""}">
                <a href="/survey/mySurvey?${condition.httpQueryString}&page.pageNo=${pageNo}">
                    ${pageNo+1}
                </a>
            </li>
        </c:forEach>
        <li class="am-pagination-next ">
            <a href="/survey/mySurvey?${condition.httpQueryString}&page.pageNo=${page.nextPage}" class="">下一页</a>
        </li>
    </ul>

    <jsp:include page="templet/footer.jsp"/>
</body>
</html>
