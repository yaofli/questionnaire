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

public class NetUtil {

	/**
	 * 获得客户端的真正IP,如果无法获得客户端的IP,则返回"".
	 * 注意: 当用户使用一些特殊技术 更改 或者 隐藏 IP时,无法获得真正的IP.
	 * 
	 * @param request 用户的HTTP请求参数
	 * @return 用户的IP.
	 */
	public static String getRealIP( HttpServletRequest request ){
		String realIp = request.getHeader( "x-forwarded-for" );
		if( !ipIsEmpty(realIp) && realIp.indexOf( ',' ) != -1 ){
			realIp = realIp.split( "," )[0];
			return realIp;
		}
		if( ipIsEmpty(realIp) ){
			realIp = request.getHeader( "Proxy-Client-IP" );
		}
		if( ipIsEmpty(realIp) ){
			realIp = request.getHeader( "WL-Proxy-Client-IP" );
		}
		if( ipIsEmpty(realIp) ){
			realIp = request.getRemoteAddr();
		}
		return realIp;
	}
	
	/**
	 * 判断一个获取的ip是否为空
	 * @param ip 获取的ip
	 * @return 是空的返回true,否则返回false.
	 */
	private static boolean ipIsEmpty( String ip ){
		return ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase( ip );
	}

}
