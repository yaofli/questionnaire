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

package git.lbk.questionnaire.sms;

import git.lbk.questionnaire.dao.BaseDao;
import git.lbk.questionnaire.entity.SmsCount;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * fixme 这个测试类太复杂了吧? 说实话, 单就这个测试类, 我就花了一个半小时找bug. 而且现在肯定还有bug...测试类这么复杂就有点背离单元测试的初衷了吧? 难道我还得写一个测试类来测试这个类的正确性吗?
 * 这里遇到的其他问题加到了question.md文件中
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:questionnaire-smsParallelTest.xml")
public class SmsImplParallelTest {

	@Autowired
	private SmsImpl sms;
	private final int threadNum = 20;
	private final CyclicBarrier barrier = new CyclicBarrier(threadNum + 1);

	@Ignore("比较耗时")
	@Test(expected = IllegalStateException.class)
	public void testSendMessage() throws Exception {
		UserSendSms[] userSendSmses = new UserSendSms[threadNum];
		for(int i = 0; i < threadNum; i++) {
			userSendSmses[i] = new UserSendSms(i);
			new Thread(userSendSmses[i], "测试sms第" + i + "线程").start();
		}
		// 通知线程开始发送短信
		barrier.await();
		// 等待所有线程发送完毕
		barrier.await();

		// 等待所有发送短信都"过期"
		Thread.sleep((sms.getSendInterval() << 10) + 2000);
		sms.cleanMobileMap();
		assertEquals(0, sms.getSendAddressMap().size());

		int sendNumSum = 0;
		BaseDao<SmsCount> smsDao = sms.getSmsDao();
		int dailyMaxSendCount = Math.min(sms.getIpDailyMaxSendCount(), sms.getMobileDailyMaxSendCount());
		for(UserSendSms userSendSms : userSendSmses) {
			int sendNum = userSendSms.getSendNum();
			if(sendNum > dailyMaxSendCount) {
				fail("发送次数大于日限制, 发送次数: " + sendNum + ", 日限制: " + dailyMaxSendCount);
			}

			sendNumSum += sendNum;
			SmsCount smsCountIp = smsDao.getEntity(userSendSms.getIp());
			assertEquals("发送次数计算错误, ip: " + userSendSms.getIp(), sendNum, smsCountIp.getCount().intValue());

			SmsCount smsCountMobile = smsDao.getEntity(userSendSms.getMobile());
			assertEquals("发送次数计算错误, mobile: " + userSendSms.getMobile(), sendNum, smsCountMobile.getCount().intValue
					());
		}
		SendSmsParallelTest sendSms = (SendSmsParallelTest) sms.getSendSms();
		assertEquals(sendNumSum, sendSms.getSendNum());

		sms.destroy();
		assertTrue("线程池没有关闭", sms.getExecutorService().isShutdown());

		sms.getTimer().schedule(new TimerTask() {
			@Override
			public void run() {
			}
		}, 10);
		fail("定时器没有关闭");

	}


	class UserSendSms implements Runnable {

		private final Random random = new Random();
		private AtomicInteger sendNum = new AtomicInteger();
		private final String mobile;
		private final String ip;
		private final long sendInterval;

		public UserSendSms(int identifier) {
			mobile = identifier + "";
			ip = identifier + "." + identifier;
			sendInterval = sms.getSendInterval() << 10;
		}

		@Override
		public void run() {
			try {
				barrier.await();
				int count = random.nextInt(15) + 1;
				long lastSendTime = System.currentTimeMillis();
				for(int i = 0; i < count; i++) {
					try {
						sms.sendMessage(mobile, mobile + "消息" + i, ip);
						sendNum.getAndIncrement();
					}
					catch(FrequentlyException e) {
						long currentTime = System.currentTimeMillis();
						if(currentTime - lastSendTime > sendInterval + 100) {
							fail("发送频率并没有高于指定的频率, 但是出现了FrequentlyException异常"
									+ "当前时间: " + currentTime
									+ ", 上次发送时间: " + lastSendTime
									+ ", 间隔: " + (currentTime - lastSendTime)
									+ ", 最小间隔: " + sendInterval + e.getMessage());
						}
					}
					catch(SendManyDailyException e) {
						// 超过最大值, 但此处不退出, 继续发送
					}
					lastSendTime = System.currentTimeMillis();
					int sleepTime = random.nextInt(4000);
					System.out.println(Thread.currentThread().getName() + "当前循环次数: " + i + ", 总次数" + count + ", " +
							"睡眠时间: " + sleepTime);
					Thread.sleep(sleepTime);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					barrier.await();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * 获取该线程成功发送的次数
		 *
		 * @return
		 */
		public int getSendNum() {
			return sendNum.get();
		}

		public String getMobile() {
			return mobile;
		}

		public String getIp() {
			return ip;
		}
	}
}

