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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie工具类
 */
public class CookieUtil {

	/**
	 * 添加cookie. path默认设置为 "/"
	 *
	 * @param response response对象
	 * @param key      cookie key
	 * @param value    cookie value
	 * @param maxAge   cookie可以存在的时间
	 */
	public static void setCookie(HttpServletResponse response, String key, String value, int maxAge) {
		setCookie(response, key, value, maxAge, "/");
	}

	/**
	 * 添加cookie
	 * @param response response对象
	 * @param key cookie key
	 * @param value cookie value
 	 * @param maxAge cookie可以存在的时间
	 * @param path cookie生效的路径
	 */
	public static void setCookie(HttpServletResponse response, String key, String value, int maxAge, String path){
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	/**
	 * 从一个cookie数组中挑选出指定名称的cookie的值.
	 *
	 * @param c    cookie数组
	 * @param name 需要的cookie的名称
	 * @return 找到则返回相应的值, 否则返回空字符串
	 */
	public static String getCookieValue(Cookie[] c, String name) {
		if(c == null) {
			return "";
		}
		String value = "";
		for(Cookie aC : c) {
			if(aC.getName().equals(name)) {
				value = aC.getValue();
				break;
			}
		}
		return value;
	}

	/**
	 * 删除cookie
	 * @param response response对象
	 * @param key 需要删除的cookie的键
	 */
	public static void deleteCookie(HttpServletResponse response, String key){
		setCookie(response, key, "", 0);
	}

	/**
	 * 自动登录cookie对应的键
	 */
	public static final String AUTO_LOGIN_COOKIE_KEY = "autoLogin";
	/**
	 * 自动登录cookie的存在时间
	 */
	public static final int AUTO_LOGIN_COOKIE_MAX_AGE = 60 * 60 * 24 * 30;

	/**
	 * 设置自动登录cookie
	 *
	 * @param response response对象
	 * @param value    自动登录码
	 */
	public static void setAutoLoginCookie(HttpServletRequest request, HttpServletResponse response, String value) {
		CookieUtil.setCookie(response, AUTO_LOGIN_COOKIE_KEY, value, AUTO_LOGIN_COOKIE_MAX_AGE, request.getContextPath()+"/");
	}

	/**
	 * 获取自动登录cookie
	 * @param request request对象
	 * @return 自动登录cookie, 或者null
	 */
	public static String getAutoLoginCookie(HttpServletRequest request){
		return getCookieValue(request.getCookies(), AUTO_LOGIN_COOKIE_KEY);
	}

	/**
	 * 删除自动登录cookie
	 *
	 * @param response response对象
	 */
	public static void deleteAutoLoginCookie(HttpServletResponse response) {
		CookieUtil.deleteCookie(response, AUTO_LOGIN_COOKIE_KEY);
	}

}
