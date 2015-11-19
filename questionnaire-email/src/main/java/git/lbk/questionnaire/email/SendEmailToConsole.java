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

import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;

/**
 * 由于经常没有没网, 所以创建这个模拟类来模拟发送邮件
 */
public class SendEmailToConsole implements SendEmail {


	@Override
	public void setSender(JavaMailSender sender) {

	}

	@Override
	public void setFrom(String from) {

	}

	@Override
	public void sendMail(EmailMessage emailMessage) throws MessagingException {
		System.out.println("send email to: " + emailMessage.getTo());
		System.out.println("subject: " + emailMessage.getSubject());
		System.out.println("message: " + emailMessage.getMessage());
	}
}
