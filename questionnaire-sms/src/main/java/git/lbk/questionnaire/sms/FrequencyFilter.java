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

package git.lbk.questionnaire.sms;import git.lbk.questionnaire.entity.Sms;
import git.lbk.questionnaire.util.RateLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 每分钟发送频率限制
 */
public class FrequencyFilter implements SmsFilter {

	private static final Logger logger = LoggerFactory.getLogger(FrequencyFilter.class);

	private static final String KEY_PREFIX = "rate.frequency.limiting:";

	private RateLimit rateLimit;
	private List<String> params = new ArrayList<>(2);

	public void setSendInterval(long sendInterval) {
		params.add(Long.toString(sendInterval));
		params.add("1");
	}

	public void setRateLimit(RateLimit rateLimit) {
		this.rateLimit = rateLimit;
	}

	@Override
	public void init() {}

	@Override
	public void filter(Sms sms) throws FrequentlyException {
		if(rateLimit.isExceedRate(KEY_PREFIX+sms.getMobile(), params)
				|| rateLimit.isExceedRate(KEY_PREFIX+sms.getIp(), params)){
			throw new FrequentlyException("发送短信过于频繁");
		}
	}

	@Override
	public void destroy() {}
}
