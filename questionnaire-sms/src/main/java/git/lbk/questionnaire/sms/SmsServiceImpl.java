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

package git.lbk.questionnaire.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 最终提供发送短信的类.
 * 可以使用一个模板发送短信
 */
public class SmsServiceImpl implements SmsService {

	private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

	private Sms sms;
	private List<SmsFilter> filters;

	/**
	 * 短信模板
	 */
	private volatile Properties template;

	public SmsServiceImpl() {
		updateTemplate();
	}

	public void setSms(Sms sms) {
		this.sms = sms;
	}

	public void setFilters(List<SmsFilter> filters) {
		this.filters = filters;
	}

	/**
	 * 更新模板内容
	 */
	@Override
	public void updateTemplate() {
		Properties properties = new Properties();
		try(InputStream inputStream = new ClassPathResource("smsTemplate.properties").getInputStream()) {
			properties.load(inputStream);
			template = properties;
		}
		catch(IOException e) {
			logger.warn("更新短信模板时发生错误", e);
		}
	}

	/**
	 * 发送验证码
	 *
	 * @param smsMessage 发送短信的基本数据
	 * @throws SendSmsFailException 发送失败时抛出该异常, 比如过于频繁, 发送次数过多等.
	 */
	@Override
	public void sendCaptcha(SmsMessage smsMessage)
			throws SendSmsFailException {
		for(SmsFilter filter : filters) {
			filter.filter(smsMessage);
		}
		if(SmsMessage.REGISTER.equals(smsMessage.getType())) {
			sendRegisterSms(smsMessage);
		}
		else {
			throw new UnknownTypeException("未知的验证码类型: " + smsMessage.getType());
		}
	}

	/**
	 * 发送注册验证码
	 *
	 * @param smsMessage 发送短信的基本数据
	 */
	private void sendRegisterSms(SmsMessage smsMessage) {
		sms.sendMessage(smsMessage.getMobile(),
				template.getProperty("register").replace("{captcha}", smsMessage.getCaptcha()));
	}

}
