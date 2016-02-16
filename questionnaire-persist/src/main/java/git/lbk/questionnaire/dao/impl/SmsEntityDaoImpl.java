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

package git.lbk.questionnaire.dao.impl;

import git.lbk.questionnaire.entity.SmsEntity;
import org.springframework.stereotype.Repository;

@Repository("smsCountDao")
public class SmsEntityDaoImpl extends BaseDaoImpl<SmsEntity> {

	public void truncate(){
		String hql = "truncate table sms";
		updateEntityBySQL(hql);
	}

	/**
	 * 获得指定手机号今天请求发送短信的次数
	 *
	 * @param mobile 用户手机号
	 * @return 今天请求发送短信的次数
	 */
	public long getMobileCount(String mobile) {
		String hql = "SELECT count(id) FROM SmsEntity WHERE mobile=? AND time >= CURDATE()";
		return (long) uniqueResult(hql, mobile);
	}

	/**
	 * 获得指定ip今天请求发送短信的次数
	 * @param ip 用户ip
	 * @return 今天请求发送短信的次数
	 */
	public long getIPCount(String ip){
		String hql = "SELECT count(id) FROM SmsEntity WHERE ip=? AND time >= CURDATE()";
		return (long) uniqueResult(hql, ip);
	}

}
