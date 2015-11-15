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

package git.lbk.questionnaire.springmvc.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CaptchaUtil {

	public static boolean validateCaptcha(HttpServletRequest request, String sessionKey, String requestName) {
		HttpSession session = request.getSession();
		String captcha = (String) session.getAttribute(sessionKey);
		session.removeAttribute(sessionKey);

		String answer = request.getParameter(requestName);

		return !(captcha == null || !captcha.equals(answer));
	}

}
