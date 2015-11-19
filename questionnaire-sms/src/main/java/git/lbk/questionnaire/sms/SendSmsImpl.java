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

import cn.ihuyi._106.Sms;
import cn.ihuyi._106.SmsSoap;
import cn.ihuyi._106.SubmitResult;

/**
 * 负责发送短信的类.
 * 该类不负责限制发送次数, 频率等问题, 只是简单的把信息发送出去
 */
public class SendSmsImpl implements SendSms{

	/**
	 * 发送失败时的最大尝试次数
	 */
	private volatile int maxTryNumber = 1;

	private String account;
	private String password;

	public void setAccount(String account) {
		this.account = account;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 设置发送失败时的最大尝试次数
	 * @param maxTryNumber 发送失败时的最大尝试次数
	 */
	@Override
	public void setMaxTryNumber(int maxTryNumber) {
		if(maxTryNumber < 1){
			return;
		}
		this.maxTryNumber = maxTryNumber;
	}

	/**
	 * 向mobile发送message消息
	 * @param mobile 手机号
	 * @param message 短信内容
	 * @return 成功返回-1, 否则返回其他值
	 */
	@Override
	public int sendMessage(String mobile, String message) {
		Sms factory = new Sms();
		SmsSoap smsSoap = factory.getSmsSoap();
		int code = 2;
		for(int i = 0; i< maxTryNumber; i++){
			SubmitResult submit = smsSoap.submit(account, password, mobile, message);
			code = submit.getCode();
			if(code == 2){
				return -1;
			}
			else{
				//fixme 如果失败了应该怎么办? 添加日志?
			}
		}
		return code;
	}

}
