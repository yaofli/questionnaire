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
import git.lbk.questionnaire.sms.FrequentlyException;
import git.lbk.questionnaire.sms.SendManyDailyException;
import git.lbk.questionnaire.sms.SmsService;
import git.lbk.questionnaire.sms.UnknownTypeException;
import git.lbk.questionnaire.util.CaptchaUtil;
import git.lbk.questionnaire.util.NetUtil;
import nl.captcha.text.producer.NumbersAnswerProducer;
import nl.captcha.text.producer.TextProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/sms")
public class SmsCaptchaController {

	private static final Logger logger = LoggerFactory.getLogger(SmsCaptchaController.class);
	/**
	 * 短信验证码在session中key
	 */
	public static final String SESSION_KEY = "__smsCaptcha__";

	public static final String REQUEST_NAME = "__smsCaptcha__";

	@Autowired
	private SmsService smsService;

	// fixme 这里直接使用了 simpleCaptcha 提供的 NumbersAnswerProducer 类来生成短信中的验证码, 这是否会导致更加依赖 simpleCaptcha. 可是难道我再实现一个吗?
	private TextProducer textProducer = new NumbersAnswerProducer(6);

	/**
	 * 发送手机短信验证码
	 * @param request 请求对象
	 * @param smsMessage 包含 手机号 和 验证码类型 的请求参数
	 * @param error 错误对象
	 * @return 返回值中key"status"对应着结果状态:
	 * <ul>
	 *  <li>如果为成功, 值为 "success"</li>
	 *  <li>信息格式不正确, 值为 "message error"</li>
	 *  <li>图片验证码不正确, 值为 "captcha error"</li>
	 *  <li>发送过于频繁, 值为 "frequently"</li>
	 *  <li>超过日最大发送次数, 值为 "exceed limit"</li>
	 *  <li>未知的验证码类型, 值为 "unknown type"</li>
	 * </ul>
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Map<String, Object> sendCaptcha(HttpServletRequest request, @Valid SmsMessage smsMessage,
	                                   BindingResult error) {
		Map<String, Object> map = new HashMap<>();
		if(error.hasErrors()){
			logger.debug(error.getAllErrors().toString());
			map.put("status", "message error");
			return map;
		}
		if(!CaptchaUtil.validateCaptcha(request, ImageCaptchaController.SESSION_KEY, ImageCaptchaController.REQUEST_NAME)) {
			map.put("status", "captcha error");
			return map;
		}
		smsMessage.setIp(NetUtil.getRealIP(request));
		smsMessage.setCaptcha(textProducer.getText());
		request.getSession().setAttribute(SESSION_KEY, smsMessage.getCaptcha());

		try {
			smsService.sendCaptcha(smsMessage);
			map.put("status", "success");
		}
		catch(FrequentlyException ex){
			map.put("status", "frequently");
		}
		catch(SendManyDailyException ex){
			map.put("status", "exceed limit");
		}
		catch(UnknownTypeException ex){
			map.put("status", "unknown type");
		}
		return map;
	}

}
