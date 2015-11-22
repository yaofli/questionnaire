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

	var userNameIsValid = false;
	var isMobile = false;
	var accountIsValid = false;
	var accountIsUsed = true;
	var passwordIsValid = false;
	var confirmPasswordIsValida = false;
	var imageCaptchaIsValid = false;
	var smsCaptchaIsValid = false;

	$('#name').blur(function(){
		if( $('#name').val().trim() == '' ){
			userNameIsValid = false;
			promptUserNameRequired();
		}
		else{
			userNameIsValid = true;
		}
	});

	/**
	 * 检验账号
	 */
	$("#account").blur(function (){
		var account = $("#account").val();
		if( account.match(/\d{11}/) ){
			isMobile = true;
			accountIsValid = true;
			$("#smsModule").removeClass('am-hide');
		}
		else if( account.match(/[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(\.\w{2,6}){1,3}/) ){
			isMobile = false;
			accountIsValid = true;
			$("#smsModule").addClass('am-hide');
		}
		else{
			promptAccountError();
			return;
		}
		checkUpAccountUsed(account);
	});

	/**
	 * 检验账号是否已经注册,
	 *  如果已经注册则设置 accountIsUsed 为 true, 并提示用户.
	 *  否则将 accountIsUsed 置为 false
	 * @param account 账号
	 */
	function checkUpAccountUsed(account){
		$.get('/user/isRegister', {account: account}, function (isUsed){
			accountIsUsed = isUsed;
			if( isUsed ){
				promptAccountUsed();
			}
		});
	}

	/**
	 * 检验密码
	 */
	$('#password').blur(function (){
		if( $('#password').val().length < 6 ){
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
			promptAccountUsed();
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
	$('#userRegister').submit(function (){
		var loading = layer.load(1, {
			shade: [0.1, '#fff']
		});

		if( !checkInput() ){
			layer.close(loading);
			return false;
		}

		var data = {
			name: $('#name').val(),
			password: $('#password').val()
		};
		if( isMobile ){
			var smsCaptcha = $('#smsCaptcha');
			data['mobile'] = $('#account').val();
			data[smsCaptcha.attr('name')] = smsCaptcha.val();
		}
		else{
			var imageCaptcha = $('#imageCaptcha');
			data['email'] = $('#account').val();
			data[imageCaptcha.attr('name')] = imageCaptcha.val();
		}

		$.post('/user/register', data, function (json){
			var status = json['status'];
			if( status != 'success' ){
				if( status == 'message error' ){
					layer.msg('信息错误, 请检查后重新注册');
				}
				else if( status == 'sms captcha error' ){
					promptSmsCaptchaError();
					$('smsCaptcha').val('');
				}
				else if( status == 'captcha error' ){
					promptImageCaptchaError();
					$('#imageCaptcha').val('');
					captcha.flush('#flushCaptcha img:first');
				}
				else if( status == 'repeat register' ){
					promptAccountUsed();
				}
			}
			else{
				if( isMobile ){
					window.location = "/WEB-INF/views/index.jsp";
				}
				else{
					window.location = "/user/registerNotify?email=" + data['email'];
				}
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
		if( accountIsUsed ){
			promptAccountUsed();
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

	function promptUserNameRequired(){
		layer.tips('用户名不能为空', '#name');
	}

	function promptAccountError(){
		layer.tips('请输入正确的 手机号/邮箱', '#account');
	}

	function promptAccountUsed(){
		if(isMobile){
			layer.tips('该手机号已经注册, 请检查是否输入错误, 或者<a href="#">重置密码</a>', '#account');
		}
		else{
			layer.tips('该邮箱已经注册, 请检查是否输入错误, 或者<a href="#">重置密码</a>', '#account');
		}
	}

	function promptPasswordRequired(){
		layer.tips('密码不能少于6位', '#password');
	}

	function promptConfirmPasswordError(){
		layer.tips('密码和确认密码不一致, 请重新输入', '#confirmPassword');
	}

	function promptImageCaptchaError(){
		layer.tips('验证码不正确', '#imageCaptcha');
	}

	function promptSmsCaptchaError(){
		layer.tips('请输入正确的短信验证码', '#smsCaptcha');
	}

});
