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
import git.lbk.questionnaire.email.SendEmailService;
import git.lbk.questionnaire.entity.EmailValidate;
import git.lbk.questionnaire.entity.User;
import git.lbk.questionnaire.ipAddress.IpActualAddressService;
import git.lbk.questionnaire.security.MessageDigestUtil;
import git.lbk.questionnaire.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.*;

public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private static final Random RAND = new SecureRandom();

	private UserDaoImpl userDao;
	private SendEmailService emailService;
	private IpActualAddressService ipActualAddressService;

	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	public void setEmailService(SendEmailService emailService) {
		this.emailService = emailService;
	}

	public void setIpActualAddressService(IpActualAddressService ipActualAddressService) {
		this.ipActualAddressService = ipActualAddressService;
	}

	/**
	 * 判断用户是否注册
	 *
	 * @param account 用户 手机号 或者 邮箱
	 * @return 如果用户已经注册, 且 已经激活 或者 账号还在有效期内, 则返回true, 否则返回false
	 */
	@Override
	public boolean isRegister(String account) {
		User user = null;
		try {
			user = getUserByAccount(account);
		}
		catch(Exception e){
			logger.error("获取用户信息时发生错误! ", e);
		}
		return user != null;
	}

	/**
	 * 用户注册
	 *
	 * @param user 用户实体对象
	 * @param ip   用户ip
	 * @return 成功返回SUCCESS, 如果邮箱/手机号已经注册, 则返回IDENTITY_USED
	 */
	@Override
	public int register(User user, String ip) {
		user.setPassword(MessageDigestUtil.SHA256(user.getPassword()));
		setAutoLoginInfo(user);
		if(user.getMobile() != null) {
			user.setStatus(User.NORMAL_STATUS);
		}
		else {
			user.setStatus(User.NOT_VERIFIED);
		}

		try {
			userDao.saveEntity(user);
			if(user.getEmail()!=null) {
				emailService.sendRegisterMail(user);
			}
			else {
				loginSuccessHandle(user, ip);
			}
		}
		catch(Exception e) {
			logger.warn(e.getMessage());
			return IDENTITY_USED;
		}
		return SUCCESS;
	}

	/**
	 * 验证用户登录信息是否匹配
	 *
	 * @param account  用户邮箱或者用户手机
	 * @param password 用户密码
	 * @param ip       用户登录ip
	 * @return 如果验证通过则返回用户的信息, 否则返回null
	 */
	@Override
	public User validateLoginInfo(String account, String password, String ip) {
		try {
			password = MessageDigestUtil.SHA256(password);
			User user = userDao.validateLoginInfo(account, password);
			return loginSuccessHandle(user, ip);
		}
		catch(Exception e){
			logger.error("验证用户登录信息时发生错误", e);
		}
		return null;
	}

	/**
	 * 验证用户自动登录信息是否匹配
	 *
	 * @param identity 用户自动登录码
	 * @param ip       用户登录ip
	 * @return 如果验证通过则返回用户的信息, 否则返回null
	 */
	@Override
	public User validateAutoLoginInfo(String identity, String ip) {
		try {
			User user = userDao.validateAutoLoginInfo(identity);
			return loginSuccessHandle(user, ip);
		}
		catch(Exception e){
			logger.error("验证用户自动登录信息时发生错误", e);
		}
		return null;
	}

	/**
	 * 根据用户邮箱或者手机号获得用户的信息. 如果用户状态为未验证邮箱类型, 且已经超过了有效期, 则删除该用户信息, 并返回null
	 *
	 * @param account 邮箱或者手机号
	 * @return 匹配的用户实体, 或者null
	 */
	private User getUserByAccount(String account) {
		User user = userDao.getUserByAccount(account);
		user = validateUser(user);
		return user;
	}

	/**
	 * 验证用户是否激活, 如果已经激活, 则记录最后登录信息. 如果没有激活但还在有效期, 则不记录最后登录信息.
	 *  否则, 删除该用户信息
	 *
	 * @param user 用户对象
	 * @param ip   ip地址
	 * @return 如果已经激活 或者 还在有效期内 则返回user, 否则返回null
	 */
	private User loginSuccessHandle(User user, String ip) {
		user = validateUser(user);
		if(user == null) {
			return null;
		}
		if(!user.getStatus().equals(User.NOT_VERIFIED)) {
			user.setLastLoginIp(ip);
			ipActualAddressService.saveIpActualInfo(user);
		}
		return user;
	}

	/**
	 * 设置用户的自动登录信息
	 *
	 * @param user user实体
	 */
	private void setAutoLoginInfo(User user) {
		user.setAutoLogin(MessageDigestUtil.SHA256(System.currentTimeMillis()
				+ ""
				+ user.getId()      // 注册时id为null
				+ user.getEmail()
				+ user.getMobile()
				+ user.getPassword()
				+ RAND.nextInt()));
	}

	/**
	 * 判断用户是否激活, 如果没有激活则删除该用户
	 *
	 * @param user 用户对象
	 * @return 如果已经激活则返回user, 否则返回null, 并删除该用户
	 */
	private User validateUser(User user) {
		if(user == null) {
			return null;
		}
		if(user.getStatus().equals(User.NOT_VERIFIED)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, -EmailValidate.EXPIRE_TIME);
			if(user.getRegisterTime().compareTo(calendar.getTime()) < 0) {
				userDao.deleteEntity(user);
				return null;
			}
		}
		return user;
	}

	/**
	 * 重新发送注册邮件
	 *
	 * @param email 邮箱
	 * @return 用户信息正确返回true, 否则返回false.
	 */
	@Override
	public boolean registerEmailSend(String email) {
		User user = null;

		try {
			user = getUserByAccount(email);
		}
		catch(Exception e){
			logger.error("获取用户信息时发生错误! ", e);
		}

		if(user == null || user.getStatus()!=User.NOT_VERIFIED) {
			return false;
		}
		emailService.sendRegisterMail(user);
		return true;
	}

	/**
	 * 通过邮箱验证码激活账号. 该方法返回之后, 如果验证码正确, 则emailValidate里会的用户信息会被设置为该验证码的用户
	 *
	 * @param mailCaptcha 邮箱验证码信息
	 * @param ip ip地址
	 * @return 该验证码关联的用户信息
	 */
	@Override
	public User activeAccount(String mailCaptcha, String ip){
		User user = emailService.validateMailCaptcha(mailCaptcha, EmailValidate.REGISTER_TYPE);
		if(user == null) {
			return null;
		}
		user.setStatus(User.NORMAL_STATUS);
		user.setLastLoginIp(ip);
		try {
			userDao.updateEntity(user);
			ipActualAddressService.saveIpActualInfo(user);
		}
		catch(Exception e){
			logger.error("更新用户信息时发生错误!", e);
			user = null;
		}
		return user;
	}

}
