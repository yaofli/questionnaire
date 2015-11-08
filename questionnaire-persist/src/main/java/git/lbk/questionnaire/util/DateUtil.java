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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类.
 */
public class DateUtil {

	/**
	 * 比较两个Date(及其子类)表示的时间是否一致.
	 * @param a date对象
	 * @param b 和a相比较的date对象
	 * @return 如果a和b同时为null或者表示的时间一致则返回true, 否则返回false
	 */
	public static boolean equals(Date a, Date b) {
		if(a == null) {
			return b == null;
		}

		if(b == null) {
			return false;
		}
		else {
			return a.getTime() == b.getTime();
		}
	}

	/**
	 * 获得时间的格式化字符串
	 *
	 * @param date 日期对象
	 * @param format 格式化字符串
	 * @return 格式化的日期字符串
	 */
	public static String format(Date date, String format){
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * 获得当前时间格式化字符串
	 * @param format 格式化字符串
	 * @return 格式化的当前日期字符串
	 */
	public static String getDate(String format){
		return format(new Date(), format);
	}
}
