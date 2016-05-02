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


import java.util.regex.Pattern;

/**
 * 提供字符串工具
 * fixme 像这种工具应该放在哪个模块里? 单独建一个模块?
 */
public class StringUtil {

	public static final Pattern MOBILE_REGEX = Pattern.compile("1[0-9]{10}");
	public static final Pattern EMAIL_REGEX = Pattern.compile("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+(\\.\\w{2,6}){1,3}");

	/**
	 * 检验手机号码格式是否正确
	 *
	 * @param mobile 手机号码
	 * @return 正确返回true, 否则返回false
	 */
	public static boolean verifyMobile(String mobile) {
		return mobile != null && MOBILE_REGEX.matcher(mobile).matches();
	}

	/**
	 * 验证邮箱格式是否正确
	 *
	 * @param email 邮箱
	 * @return 正确返回true, 否则返回false
	 */
	public static boolean verifyEmail(String email) {
		return email != null && EMAIL_REGEX.matcher(email).matches();
	}

	/**
	 * 判断给定的字符串数组中是否有空字符串(等于null 或者 等于"")
	 *
	 * @param strArray 字符数组
	 * @return 如果给定的数组为null, 或者里面有空字符串则返回true, 否则返回false
	 */
	public static boolean anyNull(String... strArray) {
		if(strArray == null) {
			return true;
		}
		for(String str : strArray) {
			if(isNull(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断给定的字符串是否为空字符串(等于null 或者 等于"")
	 *
	 * @param str 需要判断是否为空的字符串
	 * @return 如果字符串为空, 则返回true, 否则返回false
	 */
	public static boolean isNull(String str) {
		return str == null || str.equals("");
	}

	/**
	 * 储存十六进制的所有可用字符
	 */
	private final static char HEX_DIGITS[] = "0123456789ABCDEF".toCharArray();


	/**
	 * 将储存有0~15的byte数组转换为字符串形式
	 *
	 * @param b byte数组
	 * @return 数组的字符串形式
	 */
	public static String hexBytesToString(byte[] b) {
		if(b == null) {
			return "";
		}

		int length = b.length;
		StringBuilder str = new StringBuilder(length * 2);

		for(byte aB : b) {
			str.append(HEX_DIGITS[aB >> 4 & 0xf]);
			str.append(HEX_DIGITS[aB & 0xf]);
		}

		return str.toString();
	}

	/**
	 * 计算一个字符串中出现子串的次数
	 * @param originStr 原字符串
	 * @param subStr 子字符串
 	 * @return 子字符串出现的次数
	 */
	public static int subStringCount(String originStr, String subStr) {
		int count = 0;
		int index = -1;
		while((index = originStr.indexOf(subStr, index+1)) != -1) {
			count += 1;
		}
		return count;
	}

	/**
	 * 使用指定的连接符, 连接数组中所有的元素
	 * @param delimiter 连接符
	 * @param objects 对象数组
	 * @return 使用连接符连接的字符串
	 */
	public static String join(CharSequence delimiter, Object[] objects){
		if(objects.length==0){
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(objects[0]);
		for(int i=1; i<objects.length; i++){
			stringBuilder.append(delimiter).append(objects[i]);
		}
		return stringBuilder.toString();
	}

}
