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

import git.lbk.questionnaire.entity.User;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> {

	/**
	 * 获得指定邮箱或者手机号的用户信息
	 *
	 * @param account 邮箱或者手机号
	 * @return 已被注册返回true, 否则返回false
	 */
	public User getUserByAccount(String account){
		return (User) uniqueResult("from User u where (u.email = ? or u.mobile = ?)",
				account, account);
	}

	/**
	 * 验证用户登录信息是否匹配
	 *
	 * @param identity 用户邮箱或者用户手机
	 * @param password 用户密码
	 * @return 如果验证通过则返回用户的信息, 否则返回null
	 */
	public User validateLoginInfo(String identity, String password){
		return (User) uniqueResult("from User u where (u.email = ? or u.mobile = ?) and u.password = ? ",
					identity, identity, password);
	}

	/**
	 * 验证用户自动登录信息是否匹配
	 *
	 * @param autoLoginCode 用户自动登录码
	 * @return 如果验证通过则返回用户的信息, 否则返回null
	 */
	public User validateAutoLoginInfo(String autoLoginCode) {
		return (User) uniqueResult("from User u where u.autoLogin = ?", autoLoginCode);
	}

}
