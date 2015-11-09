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

import javax.mail.MessagingException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 实现异步发送邮件功能
 */
public class AsyncSendMailImpl implements AsyncSendMail {

	// fixme 像这样使用Spring封装的异步模块合适呢? 还是直接使用JDK的合适呢(sms模块)?
	// JDK的更原生, 减少对其他类库的依赖, 而Spring的可配置性实现起来貌似更简单...
	private TaskExecutor taskExecutor;
	private SendMail sendMail;

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setSendMail(SendMail sendMail) {
		this.sendMail = sendMail;
	}

	/**
	 * 异步发送邮件
	 *
	 * @param mailMessage 邮件信息
	 */
	@Override
	public void asynchronismSendMail(MailMessage mailMessage) {
		asynchronismSendMail(mailMessage, null, null);
	}

	/**
	 * 异步发送邮件
	 *
	 * @param mailMessage 邮件信息
	 * @param success     发送成功时会回调该对象的accept方法, 参数为邮件信息对象, 及mailMessage.
	 *                    如果不需要关注成功状态可以传入null
	 * @param fail        发送失败时回调该对象的accept方法, 参数分别为邮件信息对象(mailMessage) 和 异常对象.
	 *                    如果不需要关注失败状态可以传入null
	 */
	@Override
	public void asynchronismSendMail(MailMessage mailMessage,
	                                 Consumer<MailMessage> success,
	                                 BiConsumer<MailMessage, MessagingException> fail) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					sendMail.sendMail(mailMessage);
					if(success != null) {
						success.accept(mailMessage);
					}
				}
				catch(MessagingException e) {
					if(fail != null) {
						fail.accept(mailMessage, e);
					}
				}
			}
		});
	}
}
