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
<%-- 页面头部内容 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="am-topbar admin-header">
    <div class="am-topbar-brand">
        <strong><a href="<c:url value="/"/>">BK调查网</a></strong>
        <a href="<c:url value="/survey/surveySquare"/>">问卷广场</a>
    </div>
    <button class="am-topbar-btn am-topbar-toggle am-btn am-btn-sm am-btn-success am-show-sm-only"
            data-am-collapse="{target: '#topbar-collapse'}"><span class="am-sr-only">导航切换</span> <span
            class="am-icon-bars"></span></button>

    <div class="am-collapse am-topbar-collapse" id="topbar-collapse">
        <ul class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list">
            <c:if test="${sessionScope.userId == null}">
                <script src="<c:url value="/js/loadLoginPage.js"/>"></script>
                <li><a id="userLogin" href="javascript:;">登录</a></li>
                <li><a href="<c:url value="/user/registerPage"/>">注册</a></li>
            </c:if>
            <c:if test="${sessionScope.userId != null}">
                <li><a href="#"><span class="am-icon-user"></span>欢迎<c:out value="${sessionScope.userName}" escapeXml="true"/> </a></li>
                <li><a href="survey/mySurvey">我的问卷</a></li>
                <li class="am-dropdown" data-am-dropdown>
                    <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;">
                        <span class="am-icon-users"></span> 设置 <span class="am-icon-caret-down"></span>
                    </a>
                    <ul class="am-dropdown-content">
                        <li><a href="#"><span class="am-icon-cog"></span> 设置</a></li>
                        <li><a href="<c:url value="/user/logout"/>"><span class="am-icon-power-off"></span> 退出</a></li>
                    </ul>
                </li>
            </c:if>
        </ul>
    </div>
</header>
