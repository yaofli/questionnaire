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
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.Message;

import static org.junit.Assert.assertEquals;

public class SendMimeMailTest {

	private SendMail sendMimeMail;
	private GreenMail greenMail;

	@Before
	public void setUp(){
		greenMail = new GreenMail(ServerSetup.SMTP);
		greenMail.setUser("test@gamil.com", "123456");
		greenMail.start();

		ApplicationContext ctx = new ClassPathXmlApplicationContext("questionnaire-emailTest.xml");
		sendMimeMail = (SendMail) ctx.getBean("sendMimeMail");
	}

	@Test
	public void testSendMail() throws Exception {
		MailMessage mailMessage = new MailMessage();
		mailMessage.setTo("1424420612@qq.com");
		mailMessage.setSubject("subject");
		mailMessage.setMessage("test message");
		sendMimeMail.sendMail(mailMessage);

		greenMail.waitForIncomingEmail(2000, 1);

		Message[] msgs = greenMail.getReceivedMessages();

		assertEquals("test@gmail.com", msgs[0].getFrom()[0].toString());
		assertEquals(mailMessage.getSubject(), msgs[0].getSubject());
		assertEquals(mailMessage.getMessage(), GreenMailUtil.getBody(msgs[0]).trim());
	}

	@After
	public void tearDown(){
		greenMail.stop();
	}

}