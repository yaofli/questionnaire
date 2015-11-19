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

/**
 * 该接口包装了SendSms接口.
 * 实现了异步发送, 限制ip, 手机号日发送次数, 以及短时间内的发送频率
 */
public interface Sms {

	void setSmsDao(BaseDao<SmsCount> smsDao);

	void setSendSms(SendSms sendSms);

	/**
	 * 设置向同一个手机号发送短信的最短发送间隔(单位: 秒)
	 *
	 * @param sendInterval 发送的最短间隔
	 */
	void setSendInterval(long sendInterval);

	/**
	 * 设置一个ip一天可以请求发送短信的最大次数
	 *
	 * @param ipDailyMaxSendCount 一个ip一天可以请求发送的的最大次数
	 */
	void setIpDailyMaxSendCount(int ipDailyMaxSendCount);

	/**
	 * 设置一天向一个手机号发送短信的最大次数
	 *
	 * @param mobileDailyMaxSendCount 一天向一个手机号发送短信的最大次数
	 */
	void setMobileDailyMaxSendCount(int mobileDailyMaxSendCount);

	/**
	 * 设置清理发送记录的时间间隔(单位: 秒)
	 *
	 * @param clearMapInterval 清理发送记录的时间间隔
	 */
	void setClearMapInterval(long clearMapInterval);

	/**
	 * 初始化该类实例,
	 * 在调用该方法之前需要先将发送间隔, 日发送最大次数, 清理发送记录的时间间隔注入
	 */
	void init() throws Exception;

	/**
	 * 发送短信
	 *
	 * @param mobile 手机号码
	 * @param message  短信内容
	 * @param ip       请求发送短信的客户端的ip
	 * @throws FrequentlyException 如果发送过于频繁
	 * @throws SendManyDailyException 如果超过了一天发送的最大次数
	 */
	void sendMessage(String mobile, String message, String ip)
			throws FrequentlyException, SendManyDailyException;

	/**
	 * 释放资源
	 */
	void destroy();

}
