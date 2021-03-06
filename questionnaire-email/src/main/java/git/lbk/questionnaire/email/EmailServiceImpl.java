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

package git.lbk.questionnaire.email;

import git.lbk.questionnaire.dao.impl.EmailValidateDaoImpl;
import git.lbk.questionnaire.entity.EmailValidate;
import git.lbk.questionnaire.entity.User;
import git.lbk.questionnaire.util.MessageDigestUtil;
import git.lbk.questionnaire.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

import static git.lbk.questionnaire.util.DateUtil.getExcursionDate;

public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	private static final Random RAND = new SecureRandom();

	private EmailValidateDaoImpl emailDao;
	private Email email;
	private String templatePath;

	private String registerTemplate;

	public void setEmail(Email email) {
		this.email = email;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public void setEmailDao(EmailValidateDaoImpl emailDao) {
		this.emailDao = emailDao;
	}

	@Override
	public void init() throws Exception {
		registerTemplate = loadFile("registerUser.html");
	}

	private String loadFile(String fileName) throws IOException {
		ClassPathResource resource = new ClassPathResource(templatePath + File.separator + fileName);
		return FileCopyUtils.copyToString(new EncodedResource(resource, "UTF-8").getReader());
	}

	/**
	 * 发送注册验证邮件
	 *
	 * @param user 用户实体
	 */
	@Override
	public void sendRegisterMail(User user) {
		EmailValidate emailValidate = createEmailValidate(user, EmailValidate.REGISTER_TYPE);
		emailValidate.setCreateTime(user.getRegisterTime());

		Date expireTime = getExcursionDate(user.getRegisterTime(), Calendar.HOUR_OF_DAY, EmailValidate.EXPIRE_TIME);
		String emailContext = registerTemplate.replace("${username}", user.getName());
		emailContext = emailContext.replace("${captcha}", emailValidate.getIdentityCode())
				.replace("${expireTime}", DateUtil.format(expireTime, "yyyy-MM-dd HH:mm:ss"))
				.replace("${currentTime}", DateUtil.getNowDataToString("yyyy-MM-dd HH:mm:ss"))
				.replace("${registerTime}",
						DateUtil.format(user.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"))
				.replace("${type}", EmailValidate.REGISTER_TYPE);

		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setMessage(emailContext);
		emailMessage.setTo(user.getEmail());
		emailMessage.setSubject("XX账号-账号激活");
		email.sendMail(emailMessage);
		emailDao.saveEntity(emailValidate);
	}

	/**
	 * 创建一个{@link EmailValidate}对象
	 *
	 * @param user 与该EmailValidate对象关联的User对象
	 * @param type 该对象的类型
	 * @return EmailValidate对象
	 */
	private EmailValidate createEmailValidate(User user, String type) {
		EmailValidate emailValidate = new EmailValidate();
		emailValidate.setType(type);
		emailValidate.setCreateTime(new Date());
		emailValidate.setIdentityCode(createCaptcha(user.getId(), user.getName(), user.getEmail()));
		emailValidate.setUser(user);
		return emailValidate;
	}

	/**
	 * 生成邮件验证码
	 *
	 * @param userId   用户id
	 * @param userName 用户名
	 * @param email    用户邮箱
	 * @return 验证码
	 */
	private String createCaptcha(int userId, String userName, String email) {
		return MessageDigestUtil.SHA256(userId + email + System.currentTimeMillis() + userName + RAND.nextInt());
	}

	/**
	 * 根据 验证码 和 类型检验邮箱验证码, 如果正确, 则返回与之关联的用户实体, 否则返回null
	 *
	 * @param captcha 验证码
	 * @param type    类型
	 * @return 如果正确, 则返回与之关联的用户实体, 否则返回null
	 */
	@Override
	public User validateMailCaptcha(String captcha, String type) {
		EmailValidate validate = emailDao.getEntity(captcha);
		if(validate == null) {
			return null;
		}
		emailDao.deleteByUserIDAndType(validate.getUser(), validate.getType());
		if(!validate.getType().equals(type)) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -EmailValidate.EXPIRE_TIME);
		if(calendar.getTime().after(validate.getCreateTime())) {
			return null;
		}
		return validate.getUser();
	}

	/**
	 * 删除过期的邮件验证码
	 */
	public void deleteExpireCaptcha() {
		logger.info("删除过期邮件验证码");
		Date expireDate = DateUtil.getExcursionDateByNow(Calendar.HOUR_OF_DAY, -EmailValidate.EXPIRE_TIME);
		emailDao.deleteBeforeTime(expireDate);
	}

	@Override
	public void destroy() {

	}
}
