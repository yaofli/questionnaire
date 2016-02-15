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
 * 负责发送短信的类.
 * 该类不负责限制发送次数, 频率等问题, 只是简单的把信息发送出去
 */
public interface Sms {

	/**
	 * 设置发送失败时的最大尝试次数
	 *
	 * @param maxTryNumber 发送失败时的最大尝试次数
	 */
	void setMaxTryNumber(int maxTryNumber);

	/**
	 * 向mobile发送message消息
	 *
	 * @param mobile  手机号
	 * @param message 短信内容
	 * @return 成功返回-1, 否则返回其他值
	 */
	int sendMessage(String mobile, String message);

}
