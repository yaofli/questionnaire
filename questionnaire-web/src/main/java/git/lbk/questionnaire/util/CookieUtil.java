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

/**
 * cookie工具类
 */
public class CookieUtil {

	/**
	 * 从一个cookie数组中挑选出指定名称的cookie的值.
	 *
	 * @param c    cookie数组
	 * @param name 需要的cookie的名称
	 * @return 找到则返回相应的值, 否则返回空字符串
	 */
	public static String getValue(Cookie[] c, String name) {
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

}
