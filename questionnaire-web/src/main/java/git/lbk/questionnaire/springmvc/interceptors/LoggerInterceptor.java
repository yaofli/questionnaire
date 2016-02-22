/*
 * Copyright 2016 LBK
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

package git.lbk.questionnaire.springmvc.interceptors;

import git.lbk.questionnaire.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

	private final static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
			ex) throws Exception {
		if(ex == null){
			return;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("url:[").append(request.getContextPath()).append("];");
		stringBuilder.append("method:[").append(request.getMethod()).append("];");
		HttpSession session = request.getSession(false);
		if(session == null){
			stringBuilder.append("session is null;");
		}
		else{
			stringBuilder.append("session: {");
			Enumeration<String> attributeNames = session.getAttributeNames();
			while(attributeNames.hasMoreElements()){
				String attributeName = attributeNames.nextElement();
				stringBuilder.append(attributeName).append(":").append(session.getAttribute(attributeName)).append(",");
			}
			stringBuilder.append("};");
		}
		stringBuilder.append(" param:").append(request.getParameterMap()).append(";");
		stringBuilder.append("cookies:").append(CookieUtil.toString(request.getCookies()));
		logger.error(stringBuilder.toString(), ex);
	}
}
