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

package git.lbk.questionnaire.phone;

import git.lbk.questionnaire.dao.BaseDao;
import git.lbk.questionnaire.dao.impl.SmsCountDaoImpl;
import git.lbk.questionnaire.entity.SmsCount;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.fail;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * fixme 该测试类只在单线程下进行了测试. 因为我还不确定多线程该怎么测试. mock对象是否是多线程安全的? 是否需要和persist中的Dao进行集成测试?
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:questionnaire-smsTest.xml")
public class SmsImplTest {

	@Autowired
	private Sms sms;

	private BaseDao<SmsCount> smsCountMock;
	private SendSms sendSmsMock;

	private String mobile;
	private String ip;
	private String strSms;

	@Before
	public void before() {
		sendSmsMock = EasyMock.createMock(SendSms.class);
		smsCountMock = EasyMock.createMock(SmsCountDaoImpl.class);
		sms.setSendSms(sendSmsMock);
		sms.setSmsDao(smsCountMock);

		mobile =System.currentTimeMillis() + "";
		ip = "127.0.0.1" + System.currentTimeMillis();
		strSms = "测试短信";
	}

	@Test
	public void testSendMessage() throws Exception {
		EasyMock.expect(smsCountMock.getEntity(mobile)).andReturn(createSmsCount(mobile, 1));
		smsCountMock.saveEntity(createSmsCount(mobile, 2));
		expectLastCall().once();

		EasyMock.expect(smsCountMock.getEntity(ip)).andReturn(null);
		smsCountMock.saveEntity(createSmsCount(ip, 1));
		expectLastCall().once();

		EasyMock.expect(sendSmsMock.sendMessage(mobile, strSms)).andReturn(-1);
		EasyMock.replay(smsCountMock, sendSmsMock);

		sms.sendMessage(mobile, strSms, ip);

		// 睡眠1秒, 等待线程池调用发送短信方法
		Thread.sleep(1000);
		EasyMock.verify(smsCountMock, sendSmsMock);
	}

	@Test(expected = FrequentlyException.class)
	public void testSendMessageFrequently() throws Exception {
		EasyMock.expect(smsCountMock.getEntity(mobile)).andReturn(createSmsCount(mobile, 1));
		smsCountMock.saveEntity(createSmsCount(mobile, 2));
		expectLastCall().once();

		EasyMock.expect(smsCountMock.getEntity(ip)).andReturn(null);
		smsCountMock.saveEntity(createSmsCount(ip, 1));
		expectLastCall().once();

		EasyMock.expect(sendSmsMock.sendMessage(mobile, strSms)).andReturn(-1);
		EasyMock.replay(smsCountMock, sendSmsMock);

		sms.sendMessage(mobile, strSms, ip);
		sms.sendMessage(mobile, strSms, "");
	}

	@Test(expected = SendManyDailyException.class)
	public void testSendMessageMobileMany() throws Exception {
		EasyMock.expect(smsCountMock.getEntity(mobile)).andReturn(createSmsCount(mobile, 3));

		EasyMock.replay(smsCountMock);

		sms.sendMessage(mobile, strSms, ip);
	}

	@Test(expected = SendManyDailyException.class)
	public void testSendMessageIPMany() throws Exception {
		EasyMock.expect(smsCountMock.getEntity(mobile)).andReturn(createSmsCount(mobile, 1));
		smsCountMock.saveEntity(createSmsCount(mobile, 2));
		EasyMock.expect(smsCountMock.getEntity(ip)).andReturn(createSmsCount(ip, 11));

		EasyMock.replay(smsCountMock);

		sms.sendMessage(mobile, strSms, ip);
	}

	@Ignore("会销毁sms对象, 导致其他测试失败")
	@Test(expected = IllegalStateException.class)
	public void testDestroy() throws Exception {
		SmsImpl smsImpl = (SmsImpl) sms;
		sms.destroy();
		assertEquals("发送次数缓存map没有清理", 0, smsImpl.getSendDateMap().size());
		assertTrue("线程池没有关闭", smsImpl.getExecutorService().isShutdown());
		smsImpl.getTimer().schedule(new TimerTask() {
			@Override
			public void run() {
				fail("定时器没有关闭");
			}
		}, 10);

		Thread.sleep(100);
	}

	private SmsCount createSmsCount(String identity, int count) {
		SmsCount smsCount = new SmsCount();
		smsCount.setIdentity(identity);
		smsCount.setCount(count);
		return smsCount;
	}
}