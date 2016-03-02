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
import git.lbk.questionnaire.util.RateLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 每日发送次数限制.
 */
public class DailyCountFilter implements SmsFilter {
	private static final Logger logger = LoggerFactory.getLogger(DailyCountFilter.class);

	private static final String KEY_PREFIX = "rate.daily.limiting:";

	private List<String> ipParams = new ArrayList<>(2);
	private List<String> mobileParams = new ArrayList<>(2);
	private RateLimit rateLimit;

	public void setIpDailyMaxSendCount(int ipDailyMaxSendCount) {
		int secondOfDay = 60 * 60 * 24;
		ipParams.add(Integer.toString(secondOfDay));
		ipParams.add(Integer.toString(ipDailyMaxSendCount));
	}

	public void setMobileDailyMaxSendCount(int mobileDailyMaxSendCount) {
		int secondOfDay = 60 * 60 * 24;
		mobileParams.add(Integer.toString(secondOfDay));
		mobileParams.add(Integer.toString(mobileDailyMaxSendCount));
	}

	public void setRateLimit(RateLimit rateLimit) {
		this.rateLimit = rateLimit;
	}

	@Override
	public void init() {}

	@Override
	public void filter(Sms sms) throws DailySendMuchException {
		if(rateLimit.isExceedRate(KEY_PREFIX+sms.getMobile(), mobileParams)
				|| rateLimit.isExceedRate(KEY_PREFIX+sms.getIp(), ipParams)){
			throw new DailySendMuchException("发送短信过于频繁");
		}
		logger.info(sms.toString());
	}

	@Override
	public void destroy() {}

}
