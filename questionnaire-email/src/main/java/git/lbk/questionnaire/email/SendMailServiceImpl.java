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

import git.lbk.questionnaire.dao.BaseDao;
import git.lbk.questionnaire.entity.EmailValidate;
import git.lbk.questionnaire.entity.User;
import git.lbk.questionnaire.security.MessageDigestUtil;
import git.lbk.questionnaire.util.DateUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SendMailServiceImpl implements SendMailService {

	private BaseDao<EmailValidate> baseDao;
	private AsyncSendMail asyncSendMail;
	private String templatePath;

	private String registerTemplate;

	public void setAsyncSendMail(AsyncSendMail asyncSendMail) {
		this.asyncSendMail = asyncSendMail;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public void setBaseDao(BaseDao<EmailValidate> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void init() throws Exception {
		registerTemplate = loadFile("registerUser.html");
	}

	private String loadFile(String fileName) {
		ClassPathResource resource = new ClassPathResource(templatePath + File.separator + fileName);
		try {
			return FileCopyUtils.copyToString(new EncodedResource(resource, "UTF-8").getReader());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private String getMailText(String template, Map<String, String> args) {
		for(Map.Entry<String, String> arg : args.entrySet()) {
			template = template.replace("${" + arg.getKey() + "}", arg.getValue());
		}
		return template;
	}


	/**
	 * 发送注册验证邮件
	 *
	 * @param user      用户实体
	 * @param userEmail 用户邮箱
	 */
	@Override
	public void sendRegisterMail(User user, String userEmail) {
		EmailValidate emailValidate = new EmailValidate();
		emailValidate.setType(EmailValidate.REGISTED_TYPE);
		emailValidate.setCreateTime(new Date());
		emailValidate.setIdentityCode(createCaptcha(user.getId(), user.getName(), userEmail));
		emailValidate.setUser(user);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, EmailValidate.EXPIRE_TIME);
		Map<String, String> map = new HashMap<>();
		map.put("username", user.getName());
		map.put("captcha", emailValidate.getIdentityCode());
		map.put("expireTime", DateUtil.format(calendar.getTime(), "yyyy-MM-dd hh:mm:ss"));
		map.put("currentTime", DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));

		MailMessage mailMessage = new MailMessage();
		mailMessage.setMessage(getMailText(registerTemplate, map));
		mailMessage.setTo(userEmail);
		mailMessage.setSubject("XX账号-账号激活");
		asyncSendMail.asynchronismSendMail(mailMessage);
		baseDao.saveEntity(emailValidate);
	}

	private String createCaptcha(int userId, String userName, String email) {
		return MessageDigestUtil.SHA256(userId + email + System.currentTimeMillis() + userName);
	}

	/**
	 * 根据emailValidate中的验证码, 创建时间, 类型检验邮箱验证码, 如果正确, 则返回与之关联的用户实体, 否则返回null
	 *
	 * @param emailValidate 验证码
	 * @return 如果正确, 则返回与之关联的用户实体, 否则返回null
	 */
	public User validateMailCaptcha(EmailValidate emailValidate) {
		EmailValidate validate = baseDao.getEntity(emailValidate.getIdentityCode());
		if(validate == null) {
			return null;
		}
		baseDao.deleteEntity(validate);

		if(!validate.getType().equals(emailValidate.getType())) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -EmailValidate.EXPIRE_TIME);
		if(calendar.getTime().after(emailValidate.getCreateTime())) {
			return null;
		}
		return validate.getUser();
	}

	@Override
	public void destroy() {

	}
}
