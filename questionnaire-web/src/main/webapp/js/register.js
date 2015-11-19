/*
 * Copyright 2015 LBK
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

$(function (){

	var isMobile = false;
	var accountIsValid = false;
	var passwordIsValid = false;
	var confirmPasswordIsValida = false;
	var imageCaptchaIsValid = false;
	var smsCaptchaIsValid = false;

	/**
	 * 检验密码
	 * fixme 如果密码是全空格组成, 算不算合法?
	 */
	$('#password').blur(function (){
		if( $('#password').val() == '' ){
			passwordIsValid = false;
			promptPasswordRequired();
		}
		else{
			passwordIsValid = true;
		}
	});

	/**
	 * 检验确认密码
	 */
	$('#confirmPassword').blur(function (){
		if( $('#password').val() != $('#confirmPassword').val() ){
			confirmPasswordIsValida = false;
			promptConfirmPasswordError();
		}
		else{
			confirmPasswordIsValida = true;
		}
	});

	/**
	 * 检验账号
	 */
	$("#account").blur(function (){
		var account = $("#account").val();
		if( account.match(/\d{11}/) ){
			isMobile = true;
			$("#getSmsCaptcha").parent().css("display", "block");
		}
		else if( account.match(/[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(\.\w{2,6}){1,3}/) ){
			isMobile = false;
			$("#getSmsCaptcha").parent().css("display", "none");
		}
		else{
			promptAccountError();
			return;
		}
		checkUpAccountUsed(account);
	});

	/**
	 * 检查图片验证码的格式是否正确
	 */
	$('#imageCaptcha').blur(function (){
		if( $('#imageCaptcha').val().match(/\w{4}/) ){
			imageCaptchaIsValid = true;
		}
		else{
			imageCaptchaIsValid = false;
			promptImageCaptchaError();
		}
	});

	/**
	 * 请求发送手机验证码
	 */
	$('#getSmsCaptcha').click(function (){
		if( !accountIsValid ){
			promptMobileUsed();
			return false;
		}
		else if( !imageCaptchaIsValid ){
			promptImageCaptchaError();
			return false;
		}

		sms.getSmsCaptchaAndDisableButton($('#account').val(), sms.REGISTER, $('#imageCaptcha').val(),
			$('#getSmsCaptcha'), captcha.getFlushHandler('#flushCaptcha img:first', 'imageCaptcha'));
		return false;
	});

	$('#flushCaptcha').click(captcha.getFlushHandler('#flushCaptcha img:first', 'imageCaptcha'));

	/**
	 * 检查手机验证码格式
	 */
	$('#smsCaptcha').blur(function (){
		if( $("#smsCaptcha").val().match(/\d{6}/) ){
			smsCaptchaIsValid = true;
		}
		else{
			smsCaptchaIsValid = false;
			promptSmsCaptchaError();
		}
	});

	/**
	 * 提交数据
	 */
	$('#submit').click(function (){
		if( !checkInput() ){
			return false;
		}

		var data = {
			name: $('#name').val(),
			password: $('#password').val()
		};
		if( isMobile ){
			data['mobile'] = $('#account').val();
			data[$('#smsCaptcha').attr('name')] = $('#smsCaptcha').val();
		}
		else{
			data['email'] = $('#account').val();
			data[$('#imageCaptcha').attr('name')] = $('#imageCaptcha').val();
		}
		var loading = layer.load(1, {
			shade: [0.1, '#fff'] //0.1透明度的白色背景
		});

		$.post('/user/register', data, function (json){
			var status = json['status'];
			if( status != 'success' ){
				if( status == 'message error' ){
					layer.msg('信息错误, 请检查后重新注册');
				}
				else if( status == 'sms captcha error' ){
					layer.msg('短信验证码错误, 请重新输入');
					$('smsCaptcha').val('');
				}
				else if( status == 'captcha error' ){
					layer.msg('验证码错误, 请重新输入');
					$('imageCaptcha').val('');
					captcha.flush('#flushCaptcha img:first');
				}
				else if( status == 'repeat register' ){
					if( isMobile ){
						layer.msg('手机号已经注册, 经检查后重试');
					}
					else{
						layer.msg('邮箱已经注册, 经检查后重试');
					}
				}
			}
			else{
				if( isMobile ){
					//layer.msg('注册成功, 即将跳转到<a href="/">首页</a>');
					window.location = "/WEB-INF/views/index.jsp";
				}
				else{
					window.location = "/user/registerNotify?email=" + data['email'];
				}
				var exceedDate = new Date();
				exceedDate.setDate(exceedDate.getDate() + 30);
				document.cookie = "autoLogin" + "=" + json['autoLogin'] + ";expires=" + exceedDate.toGMTString();
			}
			layer.close(loading);
		}, 'json');
		return false;
	});

	function checkInput(){
		if( !accountIsValid ){
			promptAccountError()
			return false;
		}
		if( !passwordIsValid ){
			promptPasswordRequired();
			return false;
		}
		if( !confirmPasswordIsValida ){
			promptConfirmPasswordError();
			return false;
		}
		if( !isMobile && !imageCaptchaIsValid ){
			promptImageCaptchaError();
			return false;
		}
		if( isMobile && !smsCaptchaIsValid ){
			promptSmsCaptchaError();
			return false;
		}
		return true;
	}

	/**
	 * 检验账号是否已经注册,
	 *  如果已经注册则返回true, 同时设置accountIsValid为false, 并提示用户.
	 *  否则返回false, 并将accountIsValid置为true
	 * @param account 账号
	 */
	function checkUpAccountUsed(account){
		//TODO 添加ajax请求检验是否注册
		//var isUsed = false;

		var isUsed = (account == undefined);

		if( !isUsed ){
			accountIsValid = true;
			return isUsed;
		}

		accountIsValid = false;
		if( isMobile ){
			promptMobileUsed();
		}
		else{
			promptEmailUsed();
		}
		return isUsed
	}

	function promptAccountError(){
		layer.tips('请输入正确的 手机号/邮箱', '#account');
	}

	function promptEmailUsed(){
		layer.tips('该邮箱已经注册, 请检查是否输入错误, 或者<a href="#">重置密码</a>', '#account');
	}

	function promptMobileUsed(){
		layer.tips('该手机号已经注册, 请检查是否输入错误, 或者<a href="#">重置密码</a>', '#account');
	}

	function promptPasswordRequired(){
		layer.tips('请输入密码', '#password');
	}

	function promptConfirmPasswordError(){
		layer.tips('密码和确认密码不一致, 请重新输入', '#confirmPassword');
	}

	function promptImageCaptchaError(){
		layer.tips('验证码格式不正确', '#imageCaptcha');
	}

	function promptSmsCaptchaError(){
		layer.tips('请输入正确的短信验证码', '#smsCaptcha');
	}

});
