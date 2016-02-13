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

/**
 * 该文件包含验证码的工具方法, 包括用于请求发送短信验证码, 以及刷新图片验证码.
 * captcha.flush(selector):
 *      刷新图片验证码
 * captcha.getFlushHandler(captchaSelector, inputSelector):
 *      返回刷新验证码的默认事件处理器
 *
 * sms.getSmsCaptchaAndDisableButton(mobile, type, captcha, button, captchaError):
 *      用于发送短信验证码, 同时禁用触发事件的按钮
 * sms.getSmsCaptcha(type, captcha, success):
 *      用于发送短信验证码
 *
 * sms.REGISTER: 注册类型的验证码
 */
(function (){

	window['captcha'] = {};

	/**
	 * 返回刷新验证码的默认事件处理器: 刷新图片, 清空输入框
	 * @param captchaSelector 图片验证码的选择器
	 * @param inputSelector 输入框选择符
	 */
	function getFlushHandler(captchaSelector, inputSelector){
		return function(){
			flushImageCaptcha(captchaSelector);
			$(inputSelector).val('');
			return false;
		};
	}

	window['captcha']['getFlushHandler'] = getFlushHandler;

	/**
	 * 刷新验证码
	 * @param selector 验证码的选择器
	 */
	function flushImageCaptcha(selector){
		$(selector).attr('src', '/captcha.png?' + new Date().getTime());
	}
	window['captcha']['flush'] = flushImageCaptcha;

	window['sms'] = {};
	/**
	 * 短信验证码类型: 注册
	 */
	window['sms']['REGISTER'] = 'reg';

	/**
	 * 获取手机短信验证码, 同时禁用发送短信的按钮. 默认请求成功时显示提示层提醒用户
	 * @param mobile 手机号
	 * @param type 验证码类型
	 * @param captcha 图片验证码
	 * @param button 触发事件的按钮 或者 选择器
	 * @param captchaError 图片验证码错误时的回调函数, 在回调之前会提醒用户.
	 *              如果想获得更多的自由度, 请使用getSmsCaptcha(mobile, type, captcha, success)
	 * @return
	 */
	function getSmsCaptchaAndDisableButton(mobile, type, captcha, button, captchaError){
		var restore = disableButtonCycle($(button), 60);
		getSmsCaptcha(mobile, type, captcha, handelReturnMessage(function(){
			restore();
			captchaError();
		}));
		return restore;
	}

	window['sms']['getSmsCaptchaAndDisableButton'] = getSmsCaptchaAndDisableButton;

	/**
	 * 获取手机短信验证码
	 * @param mobile 手机号
	 * @param captcha 图片验证码
	 * @param type 验证码类型
	 * @param success 成功的回调函数, 可以有一个json参数. 默认为handelReturnMessage
	 */
	function getSmsCaptcha(mobile, type, captcha, success){
		if( success == undefined ){
			success = handelReturnMessage();
		}
		var data = {
			mobile: mobile,
			type: type,
			__answer__: captcha
		};
		$.get('/sms', data, success, 'json');
	}

	window['sms']['getSmsCaptcha'] = getSmsCaptcha;

	/**
	 * 创建处理返回结果的函数
	 * @param captchaError 错误类型为验证码错误时应该执行的操作.
	 */
	function handelReturnMessage(captchaError){
		var success = function (json){
			var status = json['status'];

			if( status == 'success' ){
				layer.msg('发送成功');
			}
			else if( status == 'message error' ){
				layer.msg('信息错误, 请检查手机号等信息是否有误');
			}
			else if( status == 'captcha error' ){
				layer.msg('验证码错误, 请重新输入');
				if( captchaError != undefined && captchaError instanceof Function ){
					captchaError();
				}
			}
			else if( status == 'frequently' ){
				layer.msg('发送过于频繁, 请稍等片刻');
			}
			else{
				// if(status == 'unknown type')
				// if( status == 'exceed limit' )
				layer.msg('发送了错误, 请稍后重试');
			}
		};
		return success;
	}

	/**
	 * 禁用一个按钮一小段时间.
	 * @param button 需要禁用的按钮
	 * @param time 需要禁用的时间(单位: 秒)
	 */
	function disableButtonCycle(button, time){
		var originText = button.val();
		button.attr('disabled', 'disabled');
		button.val('60');
		var timer = setInterval(function (){
			if( time == 0 ){
				restore();
			}
			else{
				time -= 1;
				$('#getSmsCaptcha').val(time);
			}
		}, 1000);
		function restore(){
			clearInterval(timer);
			button.val(originText);
			button.removeAttr('disabled');
		}
		return restore;
	}
})();