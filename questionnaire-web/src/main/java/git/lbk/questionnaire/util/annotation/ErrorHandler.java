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

package git.lbk.questionnaire.util.annotation;

import git.lbk.questionnaire.util.TokenHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * 包含拦截器发现错误时需要进行的操作
 */
public @interface ErrorHandler {


	/**
	 * 如果token不一致, 是拦截, 还是在request域中添加参数. 如果为false则直接拦截, 否则在request中添加参数, 然后放行,
	 * 之后可以使用{@link TokenHelper#hasError(HttpServletRequest)}检查是否重复提交
	 *
	 * @return 是否添加request属性, 并放行
	 */
	boolean passAddAttribute() default false;

	/**
	 * <b>注意: 该参数只有在passAddParam为false的时候才有效</b><br/>
	 * token不正确时返回给客户端的值.
	 * <ul>
	 *     <li>如果以"redirect:"开头则重定向客户端到目标页面</li>
	 *     <li>如果以"dispatcher:"开否, 则设置错误消息, 之后转发到目标页面</li>
	 *     <li>否则, 直接返回</li>
	 * </ul>
	 *
	 * @return 返回给客户端的值
	 */
	String returnValue() default "redirect:/";
}
