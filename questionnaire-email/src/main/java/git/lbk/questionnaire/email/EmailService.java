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

import git.lbk.questionnaire.entity.User;

public interface EmailService {

	void init() throws Exception;

	/**
	 * 发送注册验证邮件
	 * @param user 用户实体
	 */
	void sendRegisterMail(User user);

	/**
	 * 根据 验证码 和 类型检验邮箱验证码, 如果正确, 则返回与之关联的用户实体, 否则返回null
	 * @param captcha 验证码
	 * @param type 类型
	 * @return 如果正确, 则返回与之关联的用户实体, 否则返回null
	 */
	User validateMailCaptcha(String captcha, String type);

	/**
	 * 删除过期的邮件验证码
	 */
	void deleteExpireCaptcha();

	void destroy();

}
