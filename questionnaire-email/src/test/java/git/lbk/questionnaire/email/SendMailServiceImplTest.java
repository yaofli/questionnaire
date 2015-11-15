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

import git.lbk.questionnaire.dao.BaseDao;
import git.lbk.questionnaire.entity.EmailValidate;
import git.lbk.questionnaire.entity.User;
import org.easymock.EasyMock;
import org.easymock.LogicalOperator;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.easymock.EasyMock.expectLastCall;

public class SendMailServiceImplTest {

	private SendMailService mailService;
	private AsyncSendMail asyncSendMail;
	private BaseDao<EmailValidate> baseDao;

	@Before
	public void before() throws Exception {
		asyncSendMail = EasyMock.createMock(AsyncSendMail.class);
		baseDao = EasyMock.createMock(BaseDao.class);

		SendMailServiceImpl mailService = new SendMailServiceImpl();
		mailService.setTemplatePath("mailTemplate");
		mailService.setAsyncSendMail(asyncSendMail);
		mailService.setBaseDao(baseDao);
		mailService.init();
		this.mailService = mailService;
	}

	@Test
	public void testSendRegisterMail() throws Exception {
		MailMessage mailMessage = new MailMessage();
		mailMessage.setTo("test@gmail.com");
		mailMessage.setSubject("XX账号-账号激活");
		asyncSendMail.asynchronismSendMail(EasyMock.cmp(mailMessage, new Comparator<MailMessage>() {
			@Override
			public int compare(MailMessage o1, MailMessage o2) {
				if(!o1.getTo().equals(o2.getTo())) {
					return 1;
				}
				return o1.getSubject().compareTo(o2.getSubject());
			}
		}, LogicalOperator.EQUAL));
		baseDao.saveEntity(EasyMock.isA(EmailValidate.class));
		expectLastCall().once();

		EasyMock.replay(asyncSendMail, baseDao);

		User user = new User();
		user.setId(1);
		user.setName("abc");
		mailService.sendRegisterMail(user, mailMessage.getTo());

		Thread.sleep(1000);
		EasyMock.verify(asyncSendMail, baseDao);
	}
}