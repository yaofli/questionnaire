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

package git.lbk.questionnaire.service;

import git.lbk.questionnaire.entity.User;

public interface UserService {

	/**
	 * 判断用户是否注册
	 * @param account 用户 手机号 或者 邮箱
	 * @return 如果用户已经注册, 且 已经激活 或者 账号还在有效期内, 则返回true, 否则返回false
	 */
	boolean isRegister(String account);

	/**
	 * 用户注册
	 *
	 * @param user 用户实体对象
	 * @param ip 用户ip
	 * @return 成功返回SUCCESS, 如果邮箱/手机号已经注册, 则返回IDENTITY_USED
	 */
	void register(User user, String ip);

	/**
	 * 验证用户登录信息是否匹配
	 *
	 * @param account 用户邮箱或者用户手机
	 * @param password 用户密码
	 * @param ip 用户登录ip
	 * @return 如果验证通过则返回用户的信息, 否则返回null
	 */
	User validateLoginInfo(String account, String password, String ip);

	/**
	 * 验证用户自动登录信息是否匹配
	 *
	 * @param identity 用户自动登录码
	 * @param ip 用户登录ip
	 * @return 如果验证通过则返回用户的信息, 否则返回null
	 */
	User validateAutoLoginInfo(String identity, String ip);

	/**
	 * 通过邮箱验证码激活账号. 该方法返回之后, 如果验证码正确, 则emailValidate里会的用户信息会被设置为该验证码的用户
	 *
	 * @param mailCaptcha 邮箱验证码
	 * @param ip ip地址
	 * @return 该验证码关联的用户信息
	 */
	User activeAccount(String mailCaptcha, String ip);

	/**
	 * 重新发送注册邮件
	 * @param email 邮箱
	 * @return 用户信息正确返回true, 否则返回false.
	 */
	boolean registerEmailSend(String email);
}
