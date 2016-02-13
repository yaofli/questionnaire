/*
 * 该类改自Struts2中的org.apache.struts2.util.TokenHelper类.
 */
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.*;

/**
 * 检查重复提交的工具类
 */
public class TokenHelper {

	/**
	 * session中保存token的key的前缀
	 */
	public static final String TOKEN_NAMESPACE = "__struts.tokens__.";

	/**
	 * session保存token的键的后缀, 或者 请求域中token的名字
	 */
	public static final String DEFAULT_TOKEN_NAME = "__token__";

	public static final String ERROR_ATTRIBUTE_KEY = "__token_error__";

	private static final Logger logger = LoggerFactory.getLogger(TokenHelper.class);
	private static final Random RANDOM = new Random();

	public static String createToken(){
		return generateGUID();
	}

	/**
	 * 将token错误消息保存到request作用域内
	 * @param request request对象
	 */
	public static void addError(HttpServletRequest request){
		request.setAttribute(ERROR_ATTRIBUTE_KEY, "tokenError");
	}

	/**
	 * 查看request作用域内是否有token错误消息
	 * @param request request对象
	 * @return 有错误消息返回true, 否则返回false
	 */
	public static boolean hasError(HttpServletRequest request){
		return request.getAttribute(ERROR_ATTRIBUTE_KEY) != null;
	}

	/**
	 * 使用默认的键({@link #TOKEN_NAMESPACE} + {@link #DEFAULT_TOKEN_NAME})将token保存到session中
	 *
	 * @return token字符串
	 */
	public static String setToken(HttpSession session) {
		return setToken(session, DEFAULT_TOKEN_NAME);
	}

	/**
	 * 使用指定的键将token保存到session中.
	 *
	 * @param tokenName 该参数前面加上{@link #TOKEN_NAMESPACE}组成token在session中对应的键
	 * @return token字符串
	 */
	public static String setToken(HttpSession session, String tokenName) {
		String token = createToken();
		session.setAttribute(buildTokenSessionAttributeName(tokenName), token);
		return token;
	}

	/**
	 * 使用默认的名字构建一个带有前缀的属性名
	 *
	 * @return 命名空间前缀加上默认前缀
	 */
	public static String buildTokenSessionAttributeName() {
		return buildTokenSessionAttributeName(DEFAULT_TOKEN_NAME);
	}

	/**
	 * 使用给定的token名字构建一个带有前缀的属性名
	 *
	 * @param tokenName token名字
	 * @return 命名空间前缀加上token
	 */
	public static String buildTokenSessionAttributeName(String tokenName) {
		return TOKEN_NAMESPACE + "." + tokenName;
	}

	/**
	 * 使用默认的token名称从request中获得token参数
	 *
	 * @return token
	 */
	public static String getToken(HttpServletRequest request) {
		return getToken(request, DEFAULT_TOKEN_NAME);
	}

	/**
	 * 以tokenName为键, 从request中获得token参数
	 *
	 * @param tokenName token对应的名字
	 * @return 如果没有找到token, 则返回null, 否则返回token
	 */
	public static String getToken(HttpServletRequest request, String tokenName) {
		if(tokenName == null) {
			return null;
		}
		return request.getParameter(tokenName);
	}

	/**
	 * 验证请求域中的token是否和session中的一致
	 * @return 一致返回true, 否则返回false
	 */
	public static boolean validToken(HttpServletRequest request) {
		String tokenName = DEFAULT_TOKEN_NAME;
		String token = getToken(request ,tokenName);

		if(token == null) {
			if(logger.isDebugEnabled()) {
				logger.debug("无法从请求域中获取token, tokenName: " + tokenName);
			}
			return false;
		}

		HttpSession session = request.getSession(true);
		String tokenSessionName = buildTokenSessionAttributeName(tokenName);
		String sessionToken = (String)session.getAttribute(tokenSessionName);

		if(!token.equals(sessionToken)) {
			if(logger.isDebugEnabled()) {
				logger.debug("request中的token和session中的不匹配, sessionToken:[" + sessionToken + "], requestToken:[" + token + "]");
			}
			return false;
		}

		session.removeAttribute(tokenSessionName);

		return true;
	}

	private static String generateGUID() {
		return new BigInteger(165, RANDOM).toString(36).toUpperCase();
	}

}
