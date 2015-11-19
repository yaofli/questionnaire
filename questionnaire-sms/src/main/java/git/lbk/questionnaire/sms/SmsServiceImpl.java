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

import git.lbk.questionnaire.entity.SmsMessage;
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

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private volatile SmsImpl sms;

	/**
	 * 注册时发送短信的模板
	 */
	private volatile String register;

	public SmsServiceImpl(){
		updateTemplet();
	}

	public SmsImpl getSms() {
		return sms;
	}

	public void setSms(SmsImpl sms) {
		this.sms = sms;
	}

	/**
	 * 更新模板内容
	 * fixme 为了提高性能, 这里没有加锁, 但是会导致更新的同时会有部分用户使用的还是旧的 模板 和 用户名/密码, 那么, 是否何以牺牲一定的正确性, 来换取性能呢? 如果加锁, 这里使用读写锁应该更合适吧: 大多数时间都是在读, 偶尔写一次.
	 */
	@Override
	public void updateTemplet(){
		Properties properties = new Properties();
		try (InputStream inputStream = new ClassPathResource("smsTemplet.properties").getInputStream()){
			properties.load(inputStream);
			register = properties.getProperty("register");
		}
		catch(IOException e) {
			logger.error("更新短信模板时发生错误", e);
		}
	}

	/**
	 * 发送验证码
	 *
	 * @param smsMessage 发送短信的基本数据
	 * @throws FrequentlyException    如果发送过于频繁
	 * @throws SendManyDailyException 如果超过了一天发送的最大次数
	 * @throws UnknownTypeException    如果发送的验证码类型不存在
	 */
	@Override
	public void sendCaptcha(SmsMessage smsMessage)
			throws FrequentlyException, SendManyDailyException, UnknownTypeException {
		if(SmsMessage.REGISTER.equals(smsMessage.getType())){
			sendRegisterSms(smsMessage);
		}
		else{
			throw new UnknownTypeException("未知的验证码类型: " + smsMessage.getType());
		}
	}

	/**
	 * 发送注册验证码
	 *
	 * @param smsMessage 发送短信的基本数据
	 * @throws FrequentlyException    如果发送过于频繁
	 * @throws SendManyDailyException 如果超过了一天发送的最大次数
	 */
	private void sendRegisterSms(SmsMessage smsMessage)
			throws FrequentlyException, SendManyDailyException{
		sms.sendMessage(smsMessage.getMobile(),
				register.replace("{captcha}", smsMessage.getCaptcha()),
				smsMessage.getIp());
	}

}
