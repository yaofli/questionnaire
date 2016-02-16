/*
 * Copyright 2016 LBK
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

import git.lbk.questionnaire.entity.SmsEntity;

/**
 * 过滤短信的接口. 如果要发送的短信符合一定的规则, 则放行; 否则, 不能发送
 */
public interface SmsFilter {

	/**
	 * 初始化该过滤器
	 */
	void init();

	/**
	 * 判断短信是否可以发送. 如果不能发送, 则抛出异常
	 * @param smsEntity 将要发送的短信内容
	 * @throws SendSmsFailException 如果不可发送短信, 则抛出异常.
	 */
	void filter(SmsEntity smsEntity) throws SendSmsFailException;

	/**
	 * 销毁该过滤器
	 */
	void destroy();

}
