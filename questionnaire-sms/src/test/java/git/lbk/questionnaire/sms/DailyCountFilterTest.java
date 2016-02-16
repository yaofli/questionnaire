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

import git.lbk.questionnaire.dao.impl.SmsEntityDaoImpl;
import git.lbk.questionnaire.entity.SmsEntity;
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
	private SmsEntityDaoImpl smsCountDao;
	private SmsEntity smsEntity;

	@Before
	public void setUp() throws Exception {
		smsCountDao = EasyMock.createMock(SmsEntityDaoImpl.class);
		dailyCountFilter.setSmsDao(smsCountDao);
		smsEntity = new SmsEntity("12345678901", "127.0.0.1", SmsEntity.REGISTER_TYPE);
	}

	@Test
	public void testSendMessage() throws Exception {
		EasyMock.expect(smsCountDao.getMobileCount(smsEntity.getMobile())).andReturn(1L);
		EasyMock.expect(smsCountDao.getIPCount(smsEntity.getIp())).andReturn(5L);
		smsCountDao.saveEntity(EasyMock.anyObject());

		EasyMock.replay(smsCountDao);

		dailyCountFilter.filter(smsEntity);
	}

	@Test(expected = DailySendMuchException.class)
	public void testSendMessageMobileMany() throws Exception {
		EasyMock.expect(smsCountDao.getMobileCount(smsEntity.getMobile())).andReturn(3L);

		EasyMock.replay(smsCountDao);

		dailyCountFilter.filter(smsEntity);
	}

	@Test(expected = DailySendMuchException.class)
	public void testSendMessageIPMany() throws Exception {
		EasyMock.expect(smsCountDao.getMobileCount(smsEntity.getMobile())).andReturn(1L);
		EasyMock.expect(smsCountDao.getIPCount(smsEntity.getIp())).andReturn(11L);

		EasyMock.replay(smsCountDao);

		dailyCountFilter.filter(smsEntity);
	}

}