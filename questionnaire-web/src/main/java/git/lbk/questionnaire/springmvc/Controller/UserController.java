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

package git.lbk.questionnaire.springmvc.controller;

import git.lbk.questionnaire.email.SendMailService;
import git.lbk.questionnaire.entity.User;
import git.lbk.questionnaire.ipAddress.IpActualAddressService;
import git.lbk.questionnaire.service.UserService;
import git.lbk.questionnaire.springmvc.util.CaptchaUtil;
import git.lbk.questionnaire.springmvc.util.NetUtil;
import git.lbk.questionnaire.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Controller
public class UserController {

	public static final String SESSION_USER_ID = "user_id";
	public static final String SESSION_USER_NAME = "user_name";

	@Autowired
	private UserService userService;

	@Autowired
	private SendMailService mailService;

	@Autowired
	private IpActualAddressService ipActualAddressService;


	@RequestMapping(value = "registedPage")
	public String toRegistedPage(Map<String, Object> map){
		map.put("user", new User());
		return "registedPage";
	}


	@RequestMapping(value = "registed", method = RequestMethod.POST)
	public String registed(@Valid User user, BindingResult bindingResult, HttpServletRequest request) {
		if(bindingResult.hasErrors()) {
			return "registedPage";
		}
		if(!StringUtil.isNull(user.getMobile())) {
			if(!CaptchaUtil.validateCaptcha(request, SmsCaptcha.SESSION_KEY, SmsCaptcha.REQUEST_NAME)) {
				bindingResult.addError(new ObjectError(SmsCaptcha.REQUEST_NAME, "验证码错误"));
				return "registedPage";
			}
		}
		if(userService.registe(user) == UserService.SUCCESS) {
			HttpSession session = request.getSession();
			session.setAttribute(SESSION_USER_ID, user.getId());
			session.setAttribute(SESSION_USER_NAME, user.getName());
			user.setLastLoginIp(NetUtil.getRealIP(request));
			ipActualAddressService.saveIpActualInfo(user);
			return "/WEB-INF/index.jsp";
		}
		return "registedPage";
	}

}
