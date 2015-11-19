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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 该类继承自SendSms, 将会记录下所有发送的消息. 且并不会真的发送消息, 而是 直接忽略 或者 打印到控制台(通过ignoreSms属性控制).
 * 用来测试SmsImpl在并行环境下是否正确
 */
public class SendSmsParallelTest implements SendSms {

	private final boolean ignoreSms;
	private final AtomicInteger sendNum = new AtomicInteger(0);

	/**
	 * 初始化该类对象. 该对象会忽略所有的短信.
	 */
	public SendSmsParallelTest() {
		this(true);
	}

	/**
	 * 使用指定的ignoreSms初始化该类对象.
	 * ignoreSms: 设置调用{@link #sendMessage(String, String)}时, 是 直接忽略消息 还是 打印到控制台
	 *
	 * @param ignoreSms 如果为true, 则忽略消息, 否则打印到控制台. 默认为true
	 */
	public SendSmsParallelTest(boolean ignoreSms) {
		this.ignoreSms = ignoreSms;
	}

	/**
	 * 获取该对象被调用{@link #sendMessage(String, String)}, 也就是发送"短信" 的次数
	 * @return 调用的次数
	 */
	public int getSendNum() {
		return sendNum.get();
	}

	/**
	 * 忽略该方法的调用
	 */
	@Override
	public void setMaxTryNumber(int maxTryNumber) {
	}

	/**
	 * 向mobile发送message消息
	 *
	 * @param mobile  手机号
	 * @param message 短信内容
	 * @return 成功返回-1, 否则返回其他值
	 */
	@Override
	public int sendMessage(String mobile, String message) {
		sendNum.getAndIncrement();
		if(!ignoreSms){
			System.out.println("向[" + mobile + "]发送短信: " + message);
		}
		return -1;
	}
}
