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

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.MessagingException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.easymock.EasyMock.expectLastCall;

public class AsyncSendEmailImplTest {

	private GreenMail greenMail;

	private AsyncSendEmail asyncSendMail;
	private EmailMessage emailMessage;

	private SendEmail sendMail = EasyMock.createMock(SendEmail.class);
	private Consumer<EmailMessage> success = EasyMock.createMock(Consumer.class);
	private BiConsumer<EmailMessage, MessagingException> fail = EasyMock.createMock(BiConsumer.class);

	@Before
	public void setUp(){
		emailMessage = new EmailMessage();
		emailMessage.setTo("test@gmail.com");
		emailMessage.setSubject("主题");
		emailMessage.setMessage("测试邮件");

		// 启动greenMail保证JavaMailSenderImpl可以和服务器连接
		greenMail = new GreenMail(ServerSetup.SMTP);
		greenMail.setUser("test@gamil.com", "123456");
		greenMail.start();

		ApplicationContext ctx = new ClassPathXmlApplicationContext("questionnaire-emailTest.xml");
		asyncSendMail = (AsyncSendEmail) ctx.getBean("asyncSendMail");
		((AsyncSendEmailImpl) asyncSendMail).setSendMail(sendMail);
	}

	@Test
	public void testAsynchronismSendMail() throws Exception {
		sendMail.sendMail(emailMessage);
		expectLastCall().once();
		EasyMock.replay(sendMail);

		asyncSendMail.asynchronousSendMail(emailMessage);
		Thread.sleep(1000);

		EasyMock.verify(sendMail);
	}

	@Test
	public void testAsynchronismSendMailSuccess() throws Exception {
		sendMail.sendMail(emailMessage);
		expectLastCall().times(1);
		success.accept(emailMessage);
		expectLastCall().once();
		EasyMock.replay(sendMail, success, fail);

		asyncSendMail.asynchronousSendMail(emailMessage, success, fail);
		Thread.sleep(1000);

		EasyMock.verify(sendMail, success, fail);
	}

	@Test
	public void testAsynchronismSendMailFail() throws Exception {
		MessagingException exception = new MessagingException();
		sendMail.sendMail(emailMessage);
		expectLastCall().andThrow(exception).once();
		fail.accept(emailMessage, exception);
		expectLastCall().once();
		EasyMock.replay(sendMail, success, fail);

		asyncSendMail.asynchronousSendMail(emailMessage, success, fail);
		Thread.sleep(1000);

		EasyMock.verify(sendMail, success, fail);
	}

	@After
	public void tearDown(){
		greenMail.stop();
	}
}