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

public class AsyncEmailImplTest {

	private GreenMail greenMail;

	private AsyncEmailImpl asyncEmail;
	private EmailMessage emailMessage;

	private Email email = EasyMock.createMock(Email.class);

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
		asyncEmail = (AsyncEmailImpl) ctx.getBean("asyncEmail");
		asyncEmail.setSendMail(email);
	}

	@Test
	public void testAsynchronousSendMail() throws Exception {
		EasyMock.expect(email.sendMail(emailMessage)).andReturn(true);

		EasyMock.replay(email);

		asyncEmail.sendMail(emailMessage);
		greenMail.waitForIncomingEmail(2000, 1);

		EasyMock.verify(email);
	}

	@After
	public void tearDown(){
		greenMail.stop();
	}
}