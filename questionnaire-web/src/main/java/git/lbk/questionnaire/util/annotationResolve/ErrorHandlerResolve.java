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

package git.lbk.questionnaire.util.annotationResolve;

import git.lbk.questionnaire.util.TokenHelper;
import git.lbk.questionnaire.util.annotation.ErrorHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorHandlerResolve {

	public static final String REDIRECT_PREFIX = "redirect:";
	public static final String DISPATCHER_PREFIX = "dispatcher:";

	/**
	 * 根据errorHandler中的情况处理错误情况. 如果errorHandler为空, 则重定向到首页
	 *
	 * @param response     response对象
	 * @param request      request对象
	 * @param errorHandler ErrorHandler对象
	 * @return 放行返回true, 否则返回false
	 * @throws IOException 如果发生网络异常
	 */
	public boolean handleInvalidToken(HttpServletRequest request, HttpServletResponse response,
	                                  ErrorHandler errorHandler) throws IOException, ServletException {
		if(errorHandler == null) {
			response.sendRedirect("/");
			return false;
		}
		if(errorHandler.passAddAttribute()) {
			TokenHelper.addError(request);
			return true;
		}

		String returnValue = errorHandler.returnValue();
		if(returnValue.startsWith(REDIRECT_PREFIX)) {
			response.sendRedirect(returnValue.substring(REDIRECT_PREFIX.length()));
		}
		else if(returnValue.startsWith(DISPATCHER_PREFIX)) {
			TokenHelper.addError(request);
			request.getRequestDispatcher(returnValue.substring(DISPATCHER_PREFIX.length())).forward(request, response);
		}
		else {
			response.getWriter().print(returnValue);
		}
		return false;
	}

}
