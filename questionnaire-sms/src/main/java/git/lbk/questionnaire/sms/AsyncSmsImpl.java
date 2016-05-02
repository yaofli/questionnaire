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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncSmsImpl implements Sms {

	private static final Logger logger = LoggerFactory.getLogger(AsyncSmsImpl.class);

	public Sms sendSms;
	private ExecutorService executorService = Executors.newCachedThreadPool();

	public void setSendSms(Sms sendSms) {
		this.sendSms = sendSms;
	}

	@Override
	public void setMaxTryNumber(int maxTryNumber) {
		sendSms.setMaxTryNumber(maxTryNumber);
	}

	@Override
	public int sendMessage(String mobile, String message) {
		try {
			executorService.submit(() -> sendSms.sendMessage(mobile, message));
		}
		catch(Exception e) {
			logger.warn("提交任务时发生错误", e);
		}

		return -1;
	}
}
