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

<%-- 登录form --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form id="loginPage_userRegister" action="#" class="am-form am-form-horizontal am-margin-top-sm" method="post">
    <div class="am-form-group">
        <label for="loginPage_account" class="am-u-sm-3 am-form-label">账号</label>

        <div class="am-u-sm-9">
            <input id="loginPage_account" name="account" type="text" placeholder="手机号 / 邮箱" required="required" />
        </div>
    </div>

    <div class="am-form-group">
        <label for="loginPage_password" class="am-u-sm-3 am-form-label">密码</label>

        <div class="am-u-sm-9">
            <input id="loginPage_password" name="password" type="password" placeholder="密码" required="required" />
        </div>
    </div>

    <div class="am-form-group">
        <label for="loginPage_captcha" class="am-u-sm-3 am-form-label">验证码</label>

        <div class="am-u-sm-4">
            <input id="loginPage_captcha" name="__answer__" type="text" placeholder="验证码" required="required" />
        </div>
        <div class="am-u-sm-5">
            <a id="loginPage_flushCaptcha" class="am-u-md-4" href="#"><img src="<c:url value="/captcha.png"/>" /></a>
        </div>
    </div>

    <div class="am-form-group am-text-center">
        <input id="loginPage_autoLogin" name="password" type="checkbox" placeholder="密码" />
        <label for="loginPage_autoLogin" class="am-form-label">30天内自动登录</label>
    </div>

    <div class="am-cf">
        <input id="loginPage_register" type="submit" name="" value="登 录"
               class="am-btn am-btn-primary am-u-sm-push-2 am-u-sm-3">
        <a href="#" target="_blank" class="am-btn am-btn-default am-u-sm-3 am-u-sm-pull-2">忘记密码</a>
    </div>

    <script src="<c:url value="/js/captcha.js"/>"></script>
    <script>
        (function (){
            $('#loginPage_flushCaptcha').click(captcha.getFlushHandler('#loginPage_flushCaptcha img:first', '#loginPage_captcha'));

            $('#loginPage_userRegister').submit(function (){
                var account = $('#loginPage_account').val();
                if( !account.match(/\d{11}/)
                        && !account.match(/[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(\.\w{2,6}){1,3}/) ){
                    layer.tips('账号格式有误, 请检查您输入的账号', '#loginPage_account');
                    return false;
                }
                var password = $('#loginPage_password').val();
                if( password.length < 6 ){
                    layer.tips('密码不能短于6位', '#loginPage_password');
                    return false;
                }
                var oCaptcha = $('#loginPage_captcha');
                var captcha = oCaptcha.val();
                if( captcha.length != 4 ){
                    layer.tips('请输入正确的验证码', '#loginPage_captcha');
                    return false;
                }
                var loading = layer.load(1, {
                    shade: [0.1, '#fff']
                });
                var data = {
                    account: account,
                    password: password,
                    autoLogin: $('#loginPage_autoLogin').prop('checked')
                };
                data[oCaptcha.attr('name')] = captcha;

                $.post('<c:url value="/user/login"/>', data, function (json){
                    if( json['status'] == 'success' ){
                        var tips = '<p class="am-text-center">登录成功, 5秒后自动刷新(关闭本窗口立即刷新)</p>' +
                                '<div class="am-panel am-panel-default">' +
                                '<div class="am-panel-hd am-cf">上次登录信息</div>' +
                                '<table class="am-table am-table-bd am-table-bdrs am-table-striped am-table-hover">' +
                                '<tr><td>上次登录时间</td><td>' + json['lastLoginTime'] + '</td></tr>' +
                                '<tr><td>上次登录IP</td><td>' + json['lastLoginIp'] + '</td></tr>' +
                                '<tr><td>上次登录地点</td><td>' + json['lastLoginAddress'] + '</td></tr>' +
                                '</table>' +
                                '</div>';
                        layer.open({
                            type: 1,
                            skin: 'layui-layer-rim',
                            area: ['420px', '300px'],
                            title: '登录成功',
                            content: tips,
                            time: 5000,
                            end: function (){
                                location.reload();
                            }
                        });
                    }
                    else{
                        if( json['status'] == 'captcha error' ){
                            layer.msg('验证码错误, 重新输入');
                            oCaptcha.val('');
                        }
                        else if( json['status'] == 'password error' ){
                            layer.msg('账号或者密码错误, 请重新输入');
                        }
                        else{
                            // if(json['status'] == 'message error')
                            layer.msg('输入信息有误, 请重新输入');
                        }
                        window.captcha.flush('#loginPage_flushCaptcha img:first');
                    }
                    layer.close(loading);
                });
                return false;
            });
        })();
    </script>

</form>