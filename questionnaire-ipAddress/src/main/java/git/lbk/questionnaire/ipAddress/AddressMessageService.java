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

/**
 * 这个类基本上没有用, 但是如果没有这个类就没法用事务.
 */
public class AddressMessageService {

	private UserDaoImpl userDao;

	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	/**
	 * 更新用户最后登录信息
	 *
	 * @param user 用户对象
	 */
	public void updateUserLastLoginIp(User user) {
		userDao.updateLastLoginInfo(user);
	}
}
