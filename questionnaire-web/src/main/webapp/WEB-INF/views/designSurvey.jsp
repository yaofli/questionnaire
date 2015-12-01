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
    <title>设计问卷</title>
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

    <script src="js/tools.js"></script>
    <script src="js/jquery.min.js"></script>
    <script src="js/visualSurvey.js"></script>
    <script src="js/designSurvey.js"></script>
</head>
<body>
<jsp:include page="templet/header.jsp" />

<%--${survey}--%>

<div id="surveys" class="am-panel-group am-margin-lg">
    <div class="am-panel am-panel-default">
        <div class="am-panel am-panel-default" data-am-collapse="{target: '#designQuestion1'}">
            <div class="am-panel-hd am-cf">
                <span class="am-fl">1.</span>

                <h3 class="am-fl am-panel-title am-margin-horizontal-sm">测试问卷</h3>
                <span class="am-fl am-text-lg am-text-danger">*</span>
            </div>
            <div class="am-panel-bd">
                <label class="am-radio-inline"><input type="radio" id="q1_1" value="1" name="q1" />安徽</label>
                <label class="am-radio-inline"><input type="radio" id="q1_2" value="2" name="q1" />北京</label>
                <label class="am-radio-inline"><input type="radio" id="q1_3" value="3" name="q1" />重庆</label>
                <label class="am-radio-inline"><input type="radio" id="q1_4" value="4" name="q1" />福建</label>
                <label class="am-radio-inline"><input type="radio" id="q1_5" value="5" name="q1" />甘肃</label>
                <label class="am-radio-inline"><input type="radio" id="q1_6" value="6" name="q1" />广东</label>
                <label class="am-radio-inline"><input type="radio" id="q1_7" value="7" name="q1" />广西</label>
                <label class="am-radio-inline"><input type="radio" id="q1_8" value="8" name="q1" />贵州</label>
                <label class="am-radio-inline"><input type="radio" id="q1_9" value="9" name="q1" />海南</label>
                <label class="am-radio-inline"><input type="radio" id="q1_10" value="10" name="q1" />河北</label>
                <label class="am-radio-inline"><input type="radio" id="q1_11" value="11" name="q1" />黑龙江</label>
                <label class="am-radio-inline"><input type="radio" id="q1_12" value="12" name="q1" />河南</label>
            </div>
        </div>
        <div id="designQuestion1" class="am-panel am-panel-default am-panel-collapse am-collapse">
            <div class="am-cf am-panel-bd">
                <div class="am-u-sm-8">
                    <label>题目:</label>
                    <textarea class="am-u-sm-12" rows="3"></textarea>
                </div>
                <div>
                    <label class="am-checkbox-inline">必填题<input type="checkbox" /></label><br/>
                    <label class="am-checkbox-inline am-padding-left-0">排列方式:
                        <select>
                            <option value="0">横向自适应</option>
                            <option value="1">竖向排列</option>
                            <optgroup label="横向排列">
                                <option value="2">每行2列</option>
                                <option value="3">每行3列</option>
                                <option value="4">每行4列</option>
                                <option value="5">每行5列</option>
                                <option value="6">每行6列</option>
                                <option value="7">每行7列</option>
                                <option value="8">每行8列</option>
                                <option value="9">每行9列</option>
                                <option value="10">每行10列</option>
                                <option value="11">每行11列</option>
                                <option value="12">每行12列</option>
                                <option value="15">每行15列</option>
                                <option value="20">每行20列</option>
                                <option value="30">每行30列</option>
                            </optgroup>
                        </select>
                    </label>
                </div>
            </div>
            <div class="am-panel-bd am-cf am-padding-horizontal-lg am-padding-top-0">
                <label>选项:</label><textarea class="am-u-sm-12" rows="4"></textarea>
            </div>
        </div>
    </div>

    <div id="question1" class="am-panel am-panel-default"></div>
    <div id="question2" class="am-panel am-panel-default"></div>
</div>

<script>
    $(function (){
        var checkoutInfo = {
            title: '测试调查',
            type: 'checkbox',
            required: true,
            options: [
                {option: '安徽'},
                {option: '北京'},
                {option: '重庆'},
                {option: '福建'},
                {option: '甘肃'},
                {option: '广东'}
            ],
            style: 5
        };
        designSurvey.createQuestion('#question1', 'radio', 2);
        designSurvey.createQuestion('#question2', checkoutInfo, 3);
    });
</script>

<%--<script>
    $(function (){
        var radioInfo = {
            title: '测试调查',
            type: 'radio',
            required: true,
            options:[
                {option: '安徽'},
                {option: '北京'},
                {option: '重庆'},
                {option: '福建'},
                {option: '甘肃'},
                {option: '广东'}
            ],
            style: 5
        };
        var checkoutInfo = {
            title: '测试调查',
            type: 'checkbox',
            required: true,
            options: [
                {option: '安徽'},
                {option: '北京'},
                {option: '重庆'},
                {option: '福建'},
                {option: '甘肃'},
                {option: '广东'}
            ]
            //,
            //style: 5
        };
        var radio = visualSurvey.createQuestion('#question1', radioInfo, 2);
        var checkout = visualSurvey.createQuestion('#question2', checkoutInfo, 3);

        setTimeout(function(){
            radioInfo.options = [
                {option: 'an hui'},
                {option: 'bei jing'},
                {option: 'chong qing'},
                {option: 'fu jian'},
                {option: 'gan su'},
                {option: 'guang dong'}
            ];
            radio.freshen();
        }, 5000);
    });
</script>--%>

<jsp:include page="templet/footer.jsp" />
</body>
</html>
