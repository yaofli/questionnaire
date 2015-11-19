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

package git.lbk.questionnaire.ipAddress;

import git.lbk.questionnaire.dao.impl.UserDaoImpl;
import git.lbk.questionnaire.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 好吧, 这个类已经没用了, 我还是决定在{@link UserDaoImpl#updateLastLoginInfo(User)}方法上增加事务了
 */
public class UpdateUserLastLogin {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private UserDaoImpl userDao;

	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	/**
	 * 更新用户最后登录信息
	 * @param user 用户对象
	 */
	public void updateUserLastLoginIp(User user) {
		try {
			userDao.updateLastLoginInfo(user);
		}
		catch(Exception e) {
			logger.error("更新用户最后登录信息时发生错误", e);
		}
	}
}
