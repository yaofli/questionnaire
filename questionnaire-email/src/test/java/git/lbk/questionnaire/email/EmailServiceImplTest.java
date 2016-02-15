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

import git.lbk.questionnaire.dao.impl.EmailValidateDaoImpl;
import git.lbk.questionnaire.entity.EmailValidate;
import git.lbk.questionnaire.entity.User;
import org.easymock.EasyMock;
import org.easymock.LogicalOperator;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.easymock.EasyMock.expectLastCall;

public class EmailServiceImplTest {

	private EmailService emailService;
	private AsyncEmailImpl asyncMail;
	private EmailValidateDaoImpl baseDao;

	@Before
	public void setUp() throws Exception {
		asyncMail = EasyMock.createMock(AsyncEmailImpl.class);
		baseDao = EasyMock.createMock(EmailValidateDaoImpl.class);

		EmailServiceImpl emailService = new EmailServiceImpl();
		emailService.setTemplatePath("mailTemplate");
		emailService.setEmail(asyncMail);
		emailService.setEmailDao(baseDao);
		emailService.init();
		this.emailService = emailService;
	}

	@Test
	public void testSendRegisterMail() throws Exception {
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setTo("test@gmail.com");
		emailMessage.setSubject("XX账号-账号激活");
		asyncMail.sendMail(EasyMock.cmp(emailMessage, new Comparator<EmailMessage>() {
			@Override
			public int compare(EmailMessage o1, EmailMessage o2) {
				if(!o1.getTo().equals(o2.getTo())) {
					return 1;
				}
				return o1.getSubject().compareTo(o2.getSubject());
			}
		}, LogicalOperator.EQUAL));
		baseDao.saveEntity(EasyMock.isA(EmailValidate.class));
		expectLastCall().once();

		EasyMock.replay(asyncMail, baseDao);

		User user = new User();
		user.setId(1);
		user.setName("abc");
		emailService.sendRegisterMail(user);

		Thread.sleep(1000);
		EasyMock.verify(asyncMail, baseDao);
	}
}