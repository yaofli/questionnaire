/*
 * Copyright 2016 LBK
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

import org.hibernate.Hibernate;

import java.util.*;

public class ORMUtil {

	/**
	 * 判断给定的对象是否已经初始化
	 * @param object 需要判断是否已经初始化的对象
	 * @return 如果已经初始化了则返回true, 否则返回false
	 */
	public static boolean isInitialized(Object object){
		return Hibernate.isInitialized(object);
	}

	/**
	 * 获得对象的字符串表示形式
	 * @return 如果object已经初始化了, 则返回{@code Objects.toString(object)}; 否则, 则返回"<no initial>"
	 */
	public static String toString(Object object){
		return toString(object, "<no initial>");
	}

	/**
	 * 通过{@link Objects#toString()}方法获得对象的字符串形式. 如果该对象还没有初始化, 则返回defaultStr
	 * @param noInitialDefault 如果object没有初始化, 则返回该字符串
	 * @return 如果object已经初始化了, 则返回{@code Objects.toString(object)}; 否则, 则返回noInitialDefault
	 */
	public static String toString(Object object, String noInitialDefault){
		return isInitialized(object) ? Objects.toString(object) : noInitialDefault;
	}

}
