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

package git.lbk.questionnaire.phone;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 最终提供发送短信的类.
 * 可以使用一个模板发送短信
 */
public class SmsServiceImpl implements SmsService {

	private SmsImpl sms;

	/**
	 * 注册时发送短信的模板
	 */
	private String register;

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
	 */
	@Override
	public void updateTemplet(){
		Properties properties = new Properties();
		try (InputStream inputStream = new ClassPathResource("smsTemplet.properties").getInputStream()){
			properties.load(inputStream);
			register = properties.getProperty("register");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送注册验证码
	 * @param mobile 手机号
	 * @param userName 用户名
	 * @param captcha 验证码
	 * @param ip 客户ip
	 * @throws FrequentlyException 如果发送过于频繁
	 * @throws SendManyDailyException 如果超过了一天发送的最大次数
	 */
	@Override
	public void sendRegisterSms(String mobile, String ip, String userName, String captcha)
			throws FrequentlyException, SendManyDailyException{
		sms.sendMessage(mobile,
				register.replace("{userName}", userName).replace("{captcha}", captcha),
				ip);
	}

}
