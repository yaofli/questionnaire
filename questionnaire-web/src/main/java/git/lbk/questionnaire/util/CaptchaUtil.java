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

package git.lbk.questionnaire.util;

import git.lbk.questionnaire.springmvc.controller.ImageCaptchaController;
import git.lbk.questionnaire.springmvc.controller.SmsCaptchaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 验证码工具类. 包含的工具方法有:
 * <ul>
 *     <li>{@link #validateCaptcha(HttpServletRequest, String, String)}: 检查session中的验证码是否和请求域中一致</li>
 * </ul>
 */
public class CaptchaUtil {

	private static final Logger logger = LoggerFactory.getLogger(CaptchaUtil.class);

	/**
	 * 检验请求域中的图片验证码是否和session中的一致
	 * @param request 请求对象
	 * @return 如果session中为验证码空, 则返回false. 否则如果一致则返回true, 否则返回false
	 */
	public static boolean validateImageCaptcha(HttpServletRequest request){
		return validateCaptcha(request, ImageCaptchaController.SESSION_KEY, ImageCaptchaController.REQUEST_NAME);
	}

	/**
	 * 检验请求域中的短信验证码是否和session中的一致
	 *
	 * @param request 请求对象
	 * @return 如果session中为验证码空, 则返回false. 否则如果一致则返回true, 否则返回false
	 */
	public static boolean validateSmsCaptcha(HttpServletRequest request) {
		return validateCaptcha(request, SmsCaptchaController.SESSION_KEY, SmsCaptchaController.REQUEST_NAME);
	}

	/**
	 * 检查session中的验证码是否和传递进来的验证码一致, 同时清空session存储的验证码
	 * @param request 请求对象
	 * @param sessionKey session中存储验证码的键
	 * @param requestName 请求域中存储验证码的键
	 * @return 如果session中为验证码空, 则返回false. 否则如果一致则返回true, 否则返回false
	 */
	public static boolean validateCaptcha(HttpServletRequest request, String sessionKey, String requestName) {
		HttpSession session = request.getSession();
		String captcha = (String) session.getAttribute(sessionKey);
		session.removeAttribute(sessionKey);
		String answer = request.getParameter(requestName);

		logger.debug("session captcha: [" + captcha + "] " + ", request captcha: [" + answer + "]");
		return captcha != null && captcha.equalsIgnoreCase(answer);
	}

}
