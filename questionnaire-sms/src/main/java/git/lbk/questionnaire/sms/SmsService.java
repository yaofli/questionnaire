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

/**
 * 该接口提供最终的发送短信服务.
 * 可以使用模板发送短信
 */
public interface SmsService {

	/**
	 * 更新模板内容
	 */
	void updateTemplate();

	/**
	 * 发送验证码
	 * @param sms 发送短信的基本数据
	 * @throws SendSmsFailException 发送失败时抛出该异常, 比如过于频繁, 发送次数过多等.
	 */
	void sendCaptcha(git.lbk.questionnaire.entity.Sms sms)
			throws SendSmsFailException;
}
