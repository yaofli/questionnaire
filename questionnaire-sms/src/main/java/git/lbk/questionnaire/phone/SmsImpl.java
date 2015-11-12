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
import git.lbk.questionnaire.model.SmsCount;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;
import java.util.concurrent.*;

/**
 * 该类包装了SendSms接口.
 * 实现了异步发送, 限制ip, 手机号日发送次数, 以及短时间内的发送频率
 * fixme 这个类是否需要拆分成两/三个类? 感觉不知道怎么命名, 而且功能有点复杂.
 * 而拆成两个:一个负责异步发送, 一个负责限制次数, 就比较好命名了.
 * 但是那样的话, 类是不是太小, 太多了.
 * 就像git.lbk.questionnaire.email.AsyncSendMailImpl类(questionnaire-email模块), 感觉就太小了, 根本不像个类
 */
public class SmsImpl implements Sms, ApplicationContextAware {

	private volatile long sendInterval;
	private volatile int ipDailyMaxSendCount;
	private volatile int mobileDailyMaxSendCount;
	private long clearMapInterval;

	private ExecutorService executorService;
	private ConcurrentMap<String, Long> sendDateMap;
	private BaseDao<SmsCount> smsDao;
	private Timer timer;
	private SendSms sendSms;

	@Override
	public void setSmsDao(BaseDao<SmsCount> smsDao) {
		this.smsDao = smsDao;
	}

	@Override
	public void setSendSms(SendSms sendSms) {
		this.sendSms = sendSms;
	}

	/**
	 * 设置向同一个手机号发送短信的最短发送间隔(单位: 秒)
	 *
	 * @param sendInterval 发送的最短间隔
	 */
	@Override
	public void setSendInterval(long sendInterval) {
		this.sendInterval = sendInterval;
	}

	/**
	 * 设置一个ip一天可以请求发送短信的最大次数
	 *
	 * @param ipDailyMaxSendCount 一个ip一天可以请求发送的的最大次数
	 */
	@Override
	public void setIpDailyMaxSendCount(int ipDailyMaxSendCount) {
		this.ipDailyMaxSendCount = ipDailyMaxSendCount;
	}

	/**
	 * 设置一天向一个手机号发送短信的最大次数
	 *
	 * @param mobileDailyMaxSendCount 一天向一个手机号发送短信的最大次数
	 */
	@Override
	public void setMobileDailyMaxSendCount(int mobileDailyMaxSendCount) {
		this.mobileDailyMaxSendCount = mobileDailyMaxSendCount;
	}

	/**
	 * 设置清理发送记录的时间间隔(单位: 秒)
	 *
	 * @param clearMapInterval 清理发送记录的时间间隔
	 */
	@Override
	public void setClearMapInterval(long clearMapInterval) {
		this.clearMapInterval = clearMapInterval;
	}

	/**
	 * 初始化该类实例,
	 * 在调用该方法之前需要先将发送间隔, 日发送最大次数, 清理发送记录的时间间隔注入
	 */
	@Override
	public void init() throws Exception{
		executorService = Executors.newCachedThreadPool();
		sendDateMap = new ConcurrentHashMap<>();
		timer = new Timer("sms destroy thread");
		timer.schedule(new DestroyMobileMap(), clearMapInterval * 1000, clearMapInterval * 1000);
	}

	/**
	 * 发送短信
	 *
	 * @param mobile  手机号码
	 * @param message 短信内容
	 * @param ip      请求发送短信的客户端的ip
	 * @throws FrequentlyException    如果发送过于频繁
	 * @throws SendManyDailyException 如果超过了一天发送的最大次数
	 */
	@Override
	public void sendMessage(String mobile, String message, String ip)
			throws FrequentlyException, SendManyDailyException {
		//fixme 这段关于多线程的处理是否"正确", 就是说除了isGreatMaxCount有潜在的线程竞争之外, 还有没其他bug?
		if(isFrequently(mobile)) {
			throw new FrequentlyException("发送过于频繁: " + mobile);
		}
		else if(isFrequently(ip)) {
			throw new FrequentlyException("发送过于频繁: " + ip);
		}
		if(isGreatMaxCount(mobile, mobileDailyMaxSendCount)) {
			throw new SendManyDailyException("已超过日最大发送次数: " + mobile);
		}
		else if(isGreatMaxCount(ip, ipDailyMaxSendCount)) {
			throw new SendManyDailyException("已超过日最大发送次数: " + ip);
		}
		executorService.submit((Runnable) () -> sendSms.sendMessage(mobile, message));
	}

	/**
	 * 判断距离上次发送时间是否短于最短发送间隔了, 如果已经达到了最短间隔, 那么将发送时间置为当前时间
	 *
	 * @param mobile 发送手机号
	 * @return 短于最短发送间隔则返回true, 否则返回false
	 */
	private boolean isFrequently(String mobile) {
		Long sendTime = sendDateMap.get(mobile);
		long currentTime = System.currentTimeMillis() >> 10; // 大致相当于除1000
		if(sendTime == null) {
			return sendDateMap.putIfAbsent(mobile, currentTime) != null;
		}
		long canSendTime = sendTime + sendInterval;
		if(canSendTime < currentTime) {
			return !sendDateMap.replace(mobile, sendTime, currentTime);
		}
		return true;
	}

	/**
	 * 是否大于一天最大的发送次数.<br/>
	 * fixme 这里有个问题, 就是存在线程竞争的问题. 由于没有同步, 所以可能会出现多次请求只增加一次的问题.
	 * 如果先调用{@link #isFrequently(String)}可以在一定程度上减缓竞争出现的可能性, 但是不能完全避免.
	 *
	 * @param identity 标识符, 用于获取发送计数
	 * @param maxCount 最大的发送次数
	 * @return 如果大于最大的发送次数则返回true, 否则返回false
	 */
	private boolean isGreatMaxCount(String identity, int maxCount) {
		SmsCount smsCount = smsDao.getEntity(identity);
		if(smsCount == null) {
			smsCount = new SmsCount();
			smsCount.setIdentity(identity);
			smsCount.setCount(1);
			smsDao.saveEntity(smsCount);
			return false;
		}
		if(smsCount.getCount() < maxCount) {
			smsCount.setCount(smsCount.getCount() + 1);
			smsDao.saveEntity(smsCount);
			return false;
		}
		return true;
	}

	/**
	 * 释放资源
	 */
	@Override
	public void destroy(){
		executorService.shutdown();
		timer.cancel();
		sendDateMap.clear();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}


	private class DestroyMobileMap extends TimerTask {

		@Override
		public void run() {
			long currentTime = System.currentTimeMillis() >> 10; // 大致相当于除1000
			long expireSendTime = currentTime - sendInterval;

			for(String key : sendDateMap.keySet()) {
				Long sendTime = sendDateMap.get(key);
				if(sendTime == null) {
					continue;
				}
				if(expireSendTime > sendTime) {
					sendDateMap.remove(key, sendTime);
				}
			}
		}
	}

	//fixme 这样就直接依赖于Spring了, 是不是和Spring的非侵入相违背, 有没有办法去掉依赖?
	private ApplicationContext applicationContext;
	private String sendSmsBeanId;

	/**
	 * 更新元数据, 包括url, 用户名, 密码等信息
	 */
	@Override
	public void updateMetaData() {
		sendSms = applicationContext.getBean(sendSmsBeanId, SendSms.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setSendSmsBeanId(String sendSmsBeanId) {
		this.sendSmsBeanId = sendSmsBeanId;
	}

	/**
	 * 返回发送计数的map. 测试时用
	 *
	 * @return 发送计数的map
	 */
	Map<String, Long> getSendDateMap() {
		return Collections.unmodifiableMap(sendDateMap);
	}

	/**
	 * 返回线程池对象. 测试时用
	 *
	 * @return 线程池对象
	 */
	ExecutorService getExecutorService() {
		return executorService;
	}

	/**
	 * 返回定时器对象. 测试时用
	 *
	 * @return 定时器对象
	 */
	Timer getTimer() {
		return timer;
	}
}
