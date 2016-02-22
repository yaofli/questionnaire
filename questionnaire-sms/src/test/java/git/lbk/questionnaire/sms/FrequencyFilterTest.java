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

import git.lbk.questionnaire.entity.Sms;
import git.lbk.questionnaire.util.DateUtil;
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

/**
 * fixme 这个测试类太复杂了吧? 测试类这么复杂就有点背离单元测试的初衷了吧? 难道我还得写一个测试类来测试这个类的正确性吗?
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:questionnaire-smsTest.xml")
public class FrequencyFilterTest {

	@Autowired
	private FrequencyFilter frequencyFilter;
	private final int threadNum = 20;
	private final CyclicBarrier barrier = new CyclicBarrier(threadNum + 1);

	@Ignore("比较耗时")
	@Test(expected = IllegalStateException.class)
	public void testFilter() throws Exception {
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
		Thread.sleep(frequencyFilter.getSendInterval() + 2000);
		frequencyFilter.cleanMobileMap();
		assertEquals(0, frequencyFilter.getSendAddressMap().size());

		frequencyFilter.destroy();
		frequencyFilter.getTimer().schedule(new TimerTask() {
			@Override
			public void run() {
			}
		}, 10);
		fail("定时器没有关闭");
	}

	class UserSendSms implements Runnable {

		private final Random random = new Random();
		private AtomicInteger sendNum = new AtomicInteger();
		private final long sendInterval;
		private Sms sms;

		public UserSendSms(int identifier) {
			sendInterval = frequencyFilter.getSendInterval() + 100;
			sms = new Sms(identifier+"", identifier+"."+identifier, Sms.REGISTER_TYPE);
		}

		@Override
		public void run() {
			barrierAwait(barrier);
			try {
				int count = random.nextInt(15) + 1;
				long lastSendTime = System.currentTimeMillis();
				for(int i = 0; i < count; i++) {
					if(sendSms(lastSendTime)){
						lastSendTime = System.currentTimeMillis();
					}
					int sleepTime = random.nextInt(4000);
					System.out.println(Thread.currentThread().getName() + "当前循环次数: " + i
							+ ", 总次数: " + count + ", " + "睡眠时间: " + sleepTime);
					Thread.sleep(sleepTime);
				}
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				barrierAwait(barrier);
			}
		}

		private void barrierAwait(CyclicBarrier barrier) {
			try {
				barrier.await();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

		private boolean sendSms(long lastSendTime) throws InterruptedException {
			try {
				frequencyFilter.filter(sms);
				sendNum.getAndIncrement();
			}
			catch(FrequentlyException e) {
				long currentTime = System.currentTimeMillis();
				if(DateUtil.isBefore(lastSendTime + sendInterval, currentTime)) {
					fail("发送频率并没有高于指定的频率, 但是出现了FrequentlyException异常"
							+ "当前时间: " + currentTime
							+ ", 上次发送时间: " + lastSendTime
							+ ", 间隔: " + (currentTime - lastSendTime)
							+ ", 最小间隔: " + sendInterval);
				}
				return false;
			}
			return true;
		}

		public int getSendNum() {
			return sendNum.get();
		}

		public Sms getSms() {
			return sms;
		}
	}
}