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
<%-- 页脚 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${requestScope.user != null}">
    <script>
        var tips = '<div class="am-panel am-panel-default">' +
                '<div class="am-panel-hd am-cf">上次登录信息</div>' +
                '<table class="am-table am-table-bd am-table-bdrs am-table-striped am-table-hover">' +
                '<tr><td>上次登录时间</td><td>${requestScope.user.lastLoginTime}</td></tr>' +
                '<tr><td>上次登录IP</td><td>${requestScope.user.lastLoginIp}</td></tr>' +
                '<tr><td>上次登录地点</td><td>${requestScope.user.lastLoginAddress}</td></tr>' +
                '</table>' +
                '</div>';
        layer.open({
            type: 1,
            title: false,
            closeBtn: false,
            shade: false,
            //area: ['340px', '215px'],
            shadeClose: false,
            offset: 'rb',
            time: 5000,
            shift: 2,
            content: tips
        });
    </script>
</c:if>
<footer data-am-widget="footer" class="am-footer am-footer-default" data-am-footer="{  }">
	<div class="am-footer-switch">
		<span class="am-footer-ysp" data-rel="mobile" data-am-modal="{target: '#am-switch-mode'}">电脑版</span>
		<span class="am-footer-divider">|</span>
		<a id="godesktop" data-rel="desktop" class="am-footer-desktop"
		   href="javascript:">云适配版</a>
	</div>
	<div class="am-footer-miscs ">
		<p>由
			<a href="#" title="LBK" target="_blank" class="">LBK</a>提供技术支持</p>

	</div>
</footer>
<div id="am-footer-modal" class="am-modal am-modal-no-btn am-switch-mode-m am-switch-mode-m-default">
	<div class="am-modal-dialog">
		<div class="am-modal-hd am-modal-footer-hd">
			<a href="javascript:void(0)" data-dismiss="modal" class="am-close am-close-spin "
			   data-am-modal-close>&times;</a>
		</div>
		<div class="am-modal-bd">您正在浏览的是
			<span class="am-switch-mode-owner">电脑版</span>
			<span class="am-switch-mode-slogan">暂不支持云适配版</span>
		</div>
	</div>
</div>

<!--[if lte IE 8 ]>
<script src="<c:url value="/js/modernizr.js"/>" ></script>
<script src="<c:url value="/assets/js/amazeui.ie8polyfill.min.js"/>" ></script>
<![endif]-->
<script src="<c:url value="/assets/js/amazeui.min.js"/>"></script>
