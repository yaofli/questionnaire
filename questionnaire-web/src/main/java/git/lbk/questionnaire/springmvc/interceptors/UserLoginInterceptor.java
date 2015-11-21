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

import git.lbk.questionnaire.entity.User;
import git.lbk.questionnaire.service.UserService;
import git.lbk.questionnaire.springmvc.controller.UserController;
import git.lbk.questionnaire.util.CookieUtil;
import git.lbk.questionnaire.util.NetUtil;
import git.lbk.questionnaire.util.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录过滤拦截器. 目前只有进行自动登录.
 */
public class UserLoginInterceptor implements HandlerInterceptor {

	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		autoLogin(request, response);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
	                       Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	/**
	 * 自动登录.
	 * @param request request对象
	 * @param response response对象
	 */
	private void autoLogin(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if(session.getAttribute(UserController.SESSION_USER_ID) != null) {
			return; // 已经登录
		}
		String autoLogin = CookieUtil.getAutoLoginCookie(request);
		if(StringUtil.isNull(autoLogin)){
			return;
		}
		User user = userService.validateAutoLoginInfo(autoLogin, NetUtil.getRealIP(request));
		if(user == null){
			CookieUtil.deleteAutoLoginCookie(response);
		}
		else{
			request.setAttribute("user", user);
			session.setAttribute(UserController.SESSION_USER_ID, user.getId());
			session.setAttribute(UserController.SESSION_USER_NAME, user.getName());
			CookieUtil.setAutoLoginCookie(request, response, user.getAutoLogin());
		}
	}

}
