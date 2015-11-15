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

package git.lbk.questionnaire.service.impl;

import git.lbk.questionnaire.dao.impl.UserDaoImpl;
import git.lbk.questionnaire.email.SendMailService;
import git.lbk.questionnaire.entity.EmailValidate;
import git.lbk.questionnaire.entity.User;
import git.lbk.questionnaire.ipAddress.IpActualAddressService;
import git.lbk.questionnaire.security.MessageDigestUtil;
import git.lbk.questionnaire.service.CaptchaExpireException;
import git.lbk.questionnaire.service.UserService;

public class UserServiceImpl implements UserService {

	// fixme 这里直接使用UserDaoImpl类, 而不是它的父接口, 那么依赖注入的优势是不是就基本上没有了? 可是如果使用BaseDao的话, 那么像注册, 登录等功能就得写sql或者hql语句, 可是这不是dao层应该做的事吗?
	private UserDaoImpl userDao;
	private SendMailService sendMailService;
	private IpActualAddressService ipActualAddressService;

	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	public void setSendMailService(SendMailService sendMailService) {
		this.sendMailService = sendMailService;
	}

	public void setIpActualAddressService(IpActualAddressService ipActualAddressService) {
		this.ipActualAddressService = ipActualAddressService;
	}

	/**
	 * 验证邮箱或者手机号是否已被注册
	 *
	 * @param identity 邮箱或者手机号
	 * @return 已被注册返回true, 否则返回false
	 */
	@Override
	public boolean isRegisted(String identity) {
		return userDao.isRegisted(identity);
	}

	/**
	 * 用户注册
	 *
	 * @param user 用户实体对象
	 * @return 成功返回SUCCESS, 如果邮箱/手机号已经注册, 则返回IDENTITY_USED
	 */
	@Override
	public int registe(User user) {
		try {
			user.setPassword(MessageDigestUtil.SHA256(user.getPassword()));
			userDao.saveEntity(user);
		}
		catch(Exception e) {
			return IDENTITY_USED;
		}
		return SUCCESS;
	}

	/**
	 * 通过邮箱验证码激活账号. 该方法返回之后, 如果验证码正确, 则emailValidate里会的用户信息会被设置为该验证码的用户
	 *
	 * @param emailValidate 邮箱验证码信息
	 * @return 该验证码关联的用户信息
	 * @throws CaptchaExpireException 如果验证码已经过期, 则抛出CaptchaExpireException异常
	 */
	@Override
	public User activeAccount(EmailValidate emailValidate) throws CaptchaExpireException {
		sendMailService.validateMailCaptcha(emailValidate);
		User user = emailValidate.getUser();
		user.setStatus(User.NORMAL_STATUS);
		userDao.updateEntity(user);
		return user;
	}

	/**
	 * 验证用户登录信息是否匹配
	 *
	 * @param identity 用户邮箱或者用户手机
	 * @param password 用户密码
	 * @param ip 用户登录ip
	 * @return 如果验证通过则返回用户的信息, 否则返回null
	 */
	@Override
	public User validateLoginInfo(String identity, String password, String ip) {
		password = MessageDigestUtil.SHA256(password);
		User user = userDao.validateLoginInfo(identity, password);
		if(user != null){
			user.setLastLoginIp(ip);
			ipActualAddressService.saveIpActualInfo(user);
		}
		return user;
	}

	/**
	 * 验证用户自动登录信息是否匹配
	 *
	 * @param identity 用户自动登录码
	 * @param ip 用户登录ip
	 * @return 如果验证通过则返回用户的信息, 否则返回null
	 */
	@Override
	public User validateAutoLoginInfo(String identity, String ip) {
		User user = userDao.validateAutoLoginInfo(identity);
		if(user != null) {
			user.setLastLoginIp(ip);
			ipActualAddressService.saveIpActualInfo(user);
		}
		return user;
	}
}
