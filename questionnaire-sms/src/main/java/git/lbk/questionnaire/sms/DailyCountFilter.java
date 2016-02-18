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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class DailyCountFilter implements SmsFilter {

	private static final Logger logger = LoggerFactory.getLogger(DailyCountFilter.class);

	private volatile int ipDailyMaxSendCount;
	private volatile int mobileDailyMaxSendCount;
	private SmsEntityDaoImpl smsDao;

	public void setIpDailyMaxSendCount(int ipDailyMaxSendCount) {
		this.ipDailyMaxSendCount = ipDailyMaxSendCount;
	}

	public void setMobileDailyMaxSendCount(int mobileDailyMaxSendCount) {
		this.mobileDailyMaxSendCount = mobileDailyMaxSendCount;
	}

	public void setSmsDao(SmsEntityDaoImpl smsDao) {
		this.smsDao = smsDao;
	}

	@Override
	public void init() {}

	@Override
	@Transactional
	public void filter(SmsEntity smsMessage) throws SendSmsFailException {
		String mobile = smsMessage.getMobile();
		String ip = smsMessage.getIp();
		if(smsDao.getMobileCount(mobile) >= mobileDailyMaxSendCount) {
			throw new DailySendMuchException("发送次数超过了日发送次数");
		}
		if(smsDao.getIPCount(ip) >= ipDailyMaxSendCount) {
			throw new DailySendMuchException("发送次数超过了日发送次数");
		}
		SmsEntity smsEntity = new SmsEntity(mobile, ip, smsMessage.getType());
		smsDao.saveEntity(smsEntity);
	}

	@Override
	public void destroy() {

	}

	/**
	 * 创建新的日志表
	 */
	@Transactional
	public void createNowLogTable(){
		smsDao.createTable(1);
		smsDao.createTable(2);
		smsDao.createTable(3);
	}

}
