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

import git.lbk.questionnaire.entity.EmailValidate;
import git.lbk.questionnaire.entity.User;

public interface UserService {

	/**
	 * 成功
	 */
	int SUCCESS = 0;
	/**
	 * 邮箱或者手机号已经被占用
	 */
	int IDENTITY_USED = 1;

	/**
	 * 验证邮箱或者手机号是否已被注册
	 *
	 * @param identity 邮箱或者手机号
	 * @return 已被注册返回true, 否则返回false
	 */
	boolean isRegisted(String identity);

	/**
	 * 用户注册
	 *
	 * @param user 用户实体对象
	 * @return 成功返回SUCCESS, 如果邮箱/手机号已经注册, 则返回IDENTITY_USED
	 */
	int registe(User user);

	/**
	 * 通过邮箱验证码激活账号. 该方法返回之后, 如果验证码正确, 则emailValidate里会的用户信息会被设置为该验证码的用户
	 *
	 * @param emailValidate 邮箱验证码信息
	 * @return 该验证码关联的用户信息
	 * @throws CaptchaExpireException 如果验证码已经过期, 则抛出CaptchaExpireException异常
	 */
	User activeAccount(EmailValidate emailValidate) throws CaptchaExpireException;

	/**
	 * 验证用户登录信息是否匹配
	 *
	 * @param identity 用户邮箱或者用户手机
	 * @param password 用户密码
	 * @param ip 用户登录ip
	 * @return 如果验证通过则返回用户的信息, 否则返回null
	 */
	User validateLoginInfo(String identity, String password, String ip);

	/**
	 * 验证用户自动登录信息是否匹配
	 *
	 * @param identity 用户自动登录码
	 * @param ip 用户登录ip
	 * @return 如果验证通过则返回用户的信息, 否则返回null
	 */
	User validateAutoLoginInfo(String identity, String ip);

}
