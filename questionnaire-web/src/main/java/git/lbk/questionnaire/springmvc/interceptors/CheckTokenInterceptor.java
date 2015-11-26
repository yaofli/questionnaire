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

package git.lbk.questionnaire.springmvc.interceptors;

import git.lbk.questionnaire.util.TokenHelper;
import git.lbk.questionnaire.util.annotation.CheckToken;
import git.lbk.questionnaire.util.annotation.ErrorHandler;
import git.lbk.questionnaire.util.annotationResolve.ErrorHandlerResolve;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 检查提交的token和session中保存的是否一致
 */
public class CheckTokenInterceptor extends HandlerInterceptorAdapter {

	private ErrorHandlerResolve errorResolve;

	public void setErrorResolve(ErrorHandlerResolve errorResolve) {
		this.errorResolve = errorResolve;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();

		CheckToken checkToken = method.getAnnotation(CheckToken.class);
		if(checkToken != null) {
			boolean validate;
			synchronized(request.getSession(true)) {
				validate = TokenHelper.validToken(request);
			}
			if(!validate) {
				ErrorHandler errorHandler = checkToken.returnInfo();
				return errorResolve.handleInvalidToken(request, response, errorHandler);
			}
		}
		return true;
	}

}
