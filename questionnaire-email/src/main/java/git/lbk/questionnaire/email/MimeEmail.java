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
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * 发送mime类型的邮件
 */
public class MimeEmail implements Email {

	private JavaMailSender sender;
	private String from;

	@Override
	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

	@Override
	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public boolean sendMail(EmailMessage emailMessage){
		try {
			MimeMessage msg = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, false, "utf-8");
			helper.setFrom(from);
			helper.setTo(emailMessage.getTo());
			helper.setSubject(emailMessage.getSubject());
			helper.setSentDate(new Date());
			helper.setText(emailMessage.getMessage(), true);
			sender.send(msg);
		}
		catch(MessagingException e) {
			return false;
		}
		return true;
	}
}
