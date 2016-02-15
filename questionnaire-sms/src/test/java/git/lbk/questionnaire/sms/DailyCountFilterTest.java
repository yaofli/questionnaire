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

import git.lbk.questionnaire.dao.impl.SmsCountDaoImpl;
import git.lbk.questionnaire.entity.SmsCount;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:questionnaire-smsTest.xml")
public class DailyCountFilterTest {

	@Autowired
	private DailyCountFilter dailyCountFilter;
	private SmsCountDaoImpl smsCountDao;
	private SmsMessage smsMessage;

	@Before
	public void setUp() throws Exception {
		smsCountDao = EasyMock.createMock(SmsCountDaoImpl.class);
		dailyCountFilter.setSmsDao(smsCountDao);
		smsMessage = new SmsMessage();
		smsMessage.setIp("127.0.0.1");
		smsMessage.setMobile("12345678901");
	}

	@Test
	public void testSendMessage() throws Exception {
		String mobile = smsMessage.getMobile();
		String ip = smsMessage.getIp();
		EasyMock.expect(smsCountDao.getEntity(mobile)).andReturn(createSmsCount(mobile, 1));
		smsCountDao.saveEntity(createSmsCount(mobile, 2));
		EasyMock.expect(smsCountDao.getEntity(ip)).andReturn(createSmsCount(ip, 5));
		smsCountDao.saveEntity(createSmsCount(ip, 6));

		EasyMock.replay(smsCountDao);

		dailyCountFilter.filter(smsMessage);
	}

	@Test(expected = DailySendMuchException.class)
	public void testSendMessageMobileMany() throws Exception {
		String mobile = smsMessage.getMobile();
		EasyMock.expect(smsCountDao.getEntity(mobile)).andReturn(createSmsCount(mobile, 3));

		EasyMock.replay(smsCountDao);

		dailyCountFilter.filter(smsMessage);
	}

	@Test(expected = DailySendMuchException.class)
	public void testSendMessageIPMany() throws Exception {
		String mobile = smsMessage.getMobile();
		String ip = smsMessage.getIp();
		EasyMock.expect(smsCountDao.getEntity(mobile)).andReturn(createSmsCount(mobile, 1));
		smsCountDao.saveEntity(createSmsCount(mobile, 2));
		EasyMock.expect(smsCountDao.getEntity(ip)).andReturn(createSmsCount(ip, 11));

		EasyMock.replay(smsCountDao);

		dailyCountFilter.filter(smsMessage);
	}

	private SmsCount createSmsCount(String identity, int count) {
		SmsCount smsCount = new SmsCount();
		smsCount.setIdentity(identity);
		smsCount.setCount(count);
		return smsCount;
	}

}