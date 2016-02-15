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

package git.lbk.questionnaire.email;

import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 实现异步发送邮件功能
 */
public class AsyncEmailImpl implements Email {

	// fixme 像这样使用Spring封装的异步模块合适呢? 还是直接使用JDK的合适呢(sms模块)? JDK的更原生, 减少对其他类库的依赖, 而Spring的可配置性实现起来貌似更简单...
	private TaskExecutor taskExecutor;
	private Email sendMail;

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setSendMail(Email sendMail) {
		this.sendMail = sendMail;
	}

	@Override
	public void setSender(JavaMailSender sender) {
		sendMail.setSender(sender);
	}

	@Override
	public void setFrom(String from) {
		sendMail.setFrom(from);
	}

	@Override
	public boolean sendMail(EmailMessage emailMessage) {
		try {
			taskExecutor.execute(() -> sendMail.sendMail(emailMessage));
		}
		catch(TaskRejectedException e) {
			return false;
		}
		return true;
	}

}
