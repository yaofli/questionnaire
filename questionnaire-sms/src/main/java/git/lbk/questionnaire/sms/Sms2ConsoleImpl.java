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
 * 由于只剩几次发送短信的机会了, 所以这里使用一个类来模拟发送短信. 该类只是简单的把短信打印到控制台上
 */
public class Sms2ConsoleImpl implements Sms {

	/**
	 * 设置发送失败时的最大尝试次数
	 * @param maxTryNumber 发送失败时的最大尝试次数
	 */
	@Override
	public void setMaxTryNumber(int maxTryNumber) {}

	/**
	 * 向mobile发送message消息
	 * @param mobile 手机号
	 * @param message 短信内容
	 * @return 成功返回-1, 否则返回其他值
	 */
	@Override
	public int sendMessage(String mobile, String message) {
		System.out.println( "向 " + mobile + " 发送短信: " +  message);
		return -1;
	}

}
