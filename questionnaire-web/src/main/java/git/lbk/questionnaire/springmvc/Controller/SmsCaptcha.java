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

package git.lbk.questionnaire.springmvc.controller;

import git.lbk.questionnaire.entity.SmsMessage;
import git.lbk.questionnaire.phone.FrequentlyException;
import git.lbk.questionnaire.phone.SendManyDailyException;
import git.lbk.questionnaire.phone.SmsService;
import git.lbk.questionnaire.phone.UnknownTypeException;
import git.lbk.questionnaire.springmvc.util.CaptchaUtil;
import git.lbk.questionnaire.springmvc.util.NetUtil;
import nl.captcha.text.producer.NumbersAnswerProducer;
import nl.captcha.text.producer.TextProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class SmsCaptcha {

	/**
	 * 短信验证码在session中key
	 */
	public static final String SESSION_KEY = "__smsCaptcha__";

	public static final String REQUEST_NAME = "__smsCaptcha__";

	@Autowired
	private SmsService smsService;

	// fixme 这里直接使用了 simpleCaptcha 提供的 NumbersAnswerProducer 类来生成短信中的验证码, 这是否会导致更加依赖 simpleCaptcha. 可是难道我再实现一个吗?
	private TextProducer textProducer = new NumbersAnswerProducer();

	/**
	 * 发送验证码的状态码. 发送验证码成功
	 */
	public static final Integer SUCCESS = 0;
	/**
	 * 发送验证码的状态码. 验证码错误, 为通过图片验证, 拒绝发送验证码
	 */
	public static final Integer CAPTCHA_ERROR = 1;
	/**
	 * 发送验证码的状态码. 手机号, 用户名等信息格式不正确
	 */
	public static final Integer MESSAGE_ERROR = 2;
	/**
	 * 发送验证码的状态码. 发送过于频繁
	 */
	public static final Integer FREQUENTLY = 3;
	/**
	 * 发送验证码的状态码. 超过日最大发送次数
	 */
	public static final Integer EXCEED_LIMIT = 4;
	/**
	 * 发送验证码的状态码. 未知的验证码类型
	 */
	public static final Integer UNKNOWN_TYPE = 4;


	@RequestMapping(value = "/sendSms")
	public Map<String, Object> sendSms(HttpServletRequest request, @Valid SmsMessage smsMessage,
	                                   BindingResult result) {
		Map<String, Object> map = new HashMap<>();
		if(result.hasErrors()){
			map.put("status", MESSAGE_ERROR);
			return map;
		}
		if(CaptchaUtil.validateCaptcha(request, ImageCaptcha.SESSION_KEY, ImageCaptcha.REQUEST_NAME)) {
			map.put("status", CAPTCHA_ERROR);
			return map;
		}
		smsMessage.setIp(NetUtil.getRealIP(request));
		smsMessage.setCaptcha(textProducer.getText());
		request.getSession().setAttribute(SESSION_KEY, smsMessage.getCaptcha());

		try {
			smsService.sendCaptcha(smsMessage);
			map.put("status", SUCCESS);
		}
		catch(FrequentlyException ex){
			map.put("status", FREQUENTLY);
		}
		catch(SendManyDailyException ex){
			map.put("status", EXCEED_LIMIT);
		}
		catch(UnknownTypeException ex){
			map.put("status", UNKNOWN_TYPE);
		}
		return map;
	}

}
