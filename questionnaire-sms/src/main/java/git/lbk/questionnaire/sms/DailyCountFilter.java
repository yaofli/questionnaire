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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DailyCountFilter implements SmsFilter {

	private static final Logger logger = LoggerFactory.getLogger(DailyCountFilter.class);

	private volatile int ipDailyMaxSendCount;
	private volatile int mobileDailyMaxSendCount;
	private SmsCountDaoImpl smsDao;

	public void setIpDailyMaxSendCount(int ipDailyMaxSendCount) {
		this.ipDailyMaxSendCount = ipDailyMaxSendCount;
	}

	public void setMobileDailyMaxSendCount(int mobileDailyMaxSendCount) {
		this.mobileDailyMaxSendCount = mobileDailyMaxSendCount;
	}

	public void setSmsDao(SmsCountDaoImpl smsDao) {
		this.smsDao = smsDao;
	}

	@Override
	public void init() {
	}

	@Override
	public void filter(SmsMessage smsMessage) throws SendSmsFailException {
		if( addSendCount(smsMessage.getMobile(), mobileDailyMaxSendCount)
				&& addSendCount(smsMessage.getIp(), ipDailyMaxSendCount) ){
			return;
		}
		throw new DailySendMuchException("发送次数超过了日发送次数");
	}

	/**
	 * 增加发送次数. 如果发送次数小于maxCount, 则增加发送次数. 否则不修改任何东西
	 *
	 * @param identity 标识符, 用于获取/修改发送计数
	 * @param maxCount 最大的发送次数
	 * @return 如果修改成功, 则返回true. 否则返回false
	 */
	private boolean addSendCount(String identity, int maxCount) {
		SmsCount smsCount = smsDao.getEntity(identity);
		if(smsCount == null) {
			smsCount = new SmsCount();
			smsCount.setIdentity(identity);
			smsCount.setCount(1);
			smsDao.saveEntity(smsCount);
			return true;
		}
		if(smsCount.getCount() < maxCount) {
			smsCount.setCount(smsCount.getCount() + 1);
			smsDao.saveEntity(smsCount);
			return true;
		}
		return false;
	}

	@Override
	public void destroy() {

	}

	/**
	 * 清空发送短信计数表的数据
	 */
	public void clearData() {
		logger.info("清空短信计数数据表");
		smsDao.truncate();
	}

	/* 测试时使用的方法 */

	SmsCountDaoImpl getSmsDao() {
		return smsDao;
	}

	int getIpDailyMaxSendCount() {
		return ipDailyMaxSendCount;
	}

	int getMobileDailyMaxSendCount() {
		return mobileDailyMaxSendCount;
	}

}
