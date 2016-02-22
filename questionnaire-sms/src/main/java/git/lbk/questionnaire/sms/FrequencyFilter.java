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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static git.lbk.questionnaire.util.DateUtil.isBefore;

/**
 * 对短信发送的频率进行限制.
 */
public class FrequencyFilter implements SmsFilter {

	private static final Logger logger = LoggerFactory.getLogger(FrequencyFilter.class);

	private long sendInterval;
	private long cleanMapInterval;
	/**
	 * 存放 手机号/IP 和 上次发送时间 的键值对
	 */
	private ConcurrentMap<String, Long> sendAddressMap = new ConcurrentHashMap<>();
	private Timer timer = new Timer("sms_frequency_filter_clear_data_thread");

	/**
	 * @param sendInterval 向同一个手机号/ip发送短信的最短发送间隔(单位: 秒)
	 * @param cleanMapInterval 清理发送记录的时间间隔(单位: 秒)
	 */
	public FrequencyFilter(long sendInterval, long cleanMapInterval) {
		this.sendInterval = sendInterval * 1000;
		this.cleanMapInterval = cleanMapInterval * 1000;
	}

	public long getSendInterval() {
		return sendInterval;
	}

	@Override
	public void init() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				cleanMobileMap();
			}
		}, cleanMapInterval, cleanMapInterval);
	}

	/**
	 * 将sendAddressMap中的所有过期数据删除
	 * 在测试时需要调用, 所以不能是private.
	 */
	void cleanMobileMap() {
		logger.debug("清除过期数据");
		long currentTime = System.currentTimeMillis();
		long expireSendTime = currentTime - sendInterval;

		for(String key : sendAddressMap.keySet()) {
			Long sendTime = sendAddressMap.get(key);
			if(sendTime == null) {
				continue;
			}
			if(isBefore(sendTime, expireSendTime)) {
				sendAddressMap.remove(key, sendTime);
			}
		}
	}

	@Override
	public void filter(git.lbk.questionnaire.entity.Sms sms) throws SendSmsFailException {
		if(setSendTime(sms.getMobile()) && setSendTime(sms.getIp())){
			return;
		}
		throw new FrequentlyException("发送短信过于频繁");
	}

	/**
	 * 将发送时间修改为当前时间.
	 * 如果距离上次发送的时间间隔大于{@link #sendInterval}则设置发送时间为当前时间. 否则不修改任何内容.
	 *
	 * @param id 发送手机号 或 ip
	 * @return 如果成功将发送时间修改为当前时间, 则返回true. 否则返回false
	 */
	private boolean setSendTime(String id) {
		long currentTime = System.currentTimeMillis();

		Long sendTime = sendAddressMap.putIfAbsent(id, currentTime);
		if(sendTime == null) {
			return true;
		}

		long nextCanSendTime = sendTime + sendInterval;
		if(isBefore(currentTime, nextCanSendTime)) {
			return false;
		}

		if(sendAddressMap.replace(id, sendTime, currentTime)) {
			return true;
		}
		// 如果没有替换成功有两种情况:
		//  1. 有另外一个线程进行了替换, 那么这个线程就按失败处理
		//  2. 清理线程把map里的过期数据清零了, 那么, 就再按空进行一次放入. 如果这时不为空, 那么是肯定有一个线程放入了数据, 不论刚才是否进行了清理
		return sendAddressMap.putIfAbsent(id, currentTime) == null;
	}

	@Override
	public void destroy() {
		timer.cancel();
		sendAddressMap.clear();
	}

	// 测试时使用的方法

	Timer getTimer() {
		return timer;
	}

	Map<String, Long> getSendAddressMap() {
		return Collections.unmodifiableMap(sendAddressMap);
	}

}
