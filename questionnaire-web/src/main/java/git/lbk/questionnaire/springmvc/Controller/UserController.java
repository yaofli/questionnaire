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

import git.lbk.questionnaire.entity.EmailValidate;
import git.lbk.questionnaire.entity.User;
import git.lbk.questionnaire.service.UserService;
import git.lbk.questionnaire.util.CaptchaUtil;
import git.lbk.questionnaire.util.NetUtil;
import git.lbk.questionnaire.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String SESSION_USER_ID = "user_id";
	public static final String SESSION_USER_NAME = "user_name";

	@Autowired
	private UserService userService;

	/**
	 * 判断用户是否注册
	 * @param account 用户邮箱或者手机号
	 * @return 如果已经注册, 则返回true. 否则返回false
	 */
	@ResponseBody
	@RequestMapping(value="/isRegister", method = RequestMethod.GET)
	public String isRegister(@RequestParam(name="account") String account){
		return Boolean.toString(userService.isRegister(account));
	}

	/**
	 * 用户注册
	 *
	 * @param user          用户实体
	 * @param error 错误消息
	 * @param request       请求对象
	 * @return 返回值中key"status"对应着结果状态:
	 * <ul>
	 *  <li>如果为成功, 值为 "success"</li>
	 *  <li>信息格式不正确, 值为 "message error"</li>
	 *  <li>短信验证码不正确, 值为 "sms captcha error"</li>
	 *  <li>图片验证码不正确, 值为 "captcha error"</li>
	 *  <li>手机或者邮箱已被注册, 值为 "repeat register"</li>
	 * </ul>
	 */
	@ResponseBody
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Map<String, Object> register(@Valid User user, BindingResult error,
	                                    HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if(error.hasErrors()) {
			logger.debug(user.toString() + "\n" + error.getFieldErrors());
			map.put("status", "message error");
			return map;
		}
		if(!StringUtil.isNull(user.getMobile())) {
			if(!CaptchaUtil.validateCaptcha(request, SmsCaptchaController.SESSION_KEY, SmsCaptchaController.REQUEST_NAME)) {
				map.put("status", "sms captcha error");
				return map;
			}
		}
		else {
			if(!CaptchaUtil.validateCaptcha(request, ImageCaptchaController.SESSION_KEY, ImageCaptchaController.REQUEST_NAME)) {
				map.put("status", "captcha error");
				return map;
			}
		}
		user.setType(User.COMMON);
		user.setRegisterTime(new Date());
		if(userService.register(user, NetUtil.getRealIP(request)) == UserService.SUCCESS) {
			saveUserToSession(user, request.getSession());
			map.put("status", "success");
			map.put("autoLogin", user.getAutoLogin());
		}
		else {
			map.put("status", "repeat register");
		}
		return map;
	}

	/**
	 * 用户登录成功时调用, 进行后续处理. 包括保存最后登录ip, 保存用户信息到session中
	 * @param user 用户对象
	 */
	private void saveUserToSession(User user, HttpSession session){
		session.setAttribute(SESSION_USER_ID, user.getId());
		session.setAttribute(SESSION_USER_NAME, user.getName());
	}


	/**
	 * 用来存储特殊的邮箱域名和服务器url的映射关系
	 */
	private final Properties mailMap;

	{
		Properties tmp = null;
		try(InputStream inputStream = new ClassPathResource("emailUrl.properties").getInputStream()) {
			tmp = new Properties();
			tmp.load(inputStream);
		}
		catch(Exception e) {
			logger.error("无法加载邮箱域名和服务器url的映射关系", e);
		}
		mailMap = tmp;
	}

	/**
	 * 邮箱注册成功, 跳转到激活邮箱提示页
	 * @param email 注册邮箱
	 * @param map 用来返回注册邮箱对应的邮箱服务器url
	 * @return 激活邮箱提示页
	 */
	@RequestMapping(value = "registerNotify", method = RequestMethod.GET)
	public String toRegisterNotify(@RequestParam("email") String email, Map<String, Object> map){
		String domain = email.substring(email.indexOf("@") + 1);
		map.put("emailUrl", mailMap.getProperty(domain, "http://mail." + domain));
		return "registerNotify";
	}

	/**
	 * 重新发送注册激活邮件
	 * @param email 注册时的邮箱
	 * @return 返回一个代表状态的字符串
	 * <ul>
	 *     <li>成功时返回: success</li>
	 *     <li>传递的参数不是email时返回: account error</li>
	 *     <li>传递email不是账户时返回: invalid email</li>
	 * </ul>
	 */
	@ResponseBody
	@RequestMapping(value="registerEmailSend")
	public String registerEmailSend(@RequestParam(value = "email", required = false)String email){
		if(!StringUtil.verifyEmail(email)){
			return "account error";
		}
		if( userService.registerEmailSend(email) ) {
			return "success";
		}
		else{
			return "invalid email";
		}
	}

	/**
	 * 激活账号
	 * @return 返回到首页
	 */
	@RequestMapping(value="actionAccount")
	public String actionAccount(@Valid EmailValidate emailValidate, BindingResult error, HttpServletRequest request){
		if(error.hasErrors()
				|| !emailValidate.getType().equals(EmailValidate.REGISTER_TYPE)){
			request.setAttribute("error", "error");
			return "/actionAccount";
		}
		User user = userService.activeAccount(emailValidate.getIdentityCode(), NetUtil.getRealIP(request));
		if(user!=null){
			saveUserToSession(user, request.getSession());
			return "/actionAccount";
		}
		request.setAttribute("error", "error");
		return "/actionAccount";
	}

}
