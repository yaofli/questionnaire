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

package git.lbk.questionnaire.service;

import git.lbk.questionnaire.entity.Page;
import git.lbk.questionnaire.entity.Survey;

import java.util.*;


public interface SurveyService {

	/**
	 * 获取某个用户的所有调查问卷的基本信息
	 * @param userId 用户id
	 * @return 该用户的所有调查问卷
	 */
	List<Survey> getSurveyByUserId(Integer userId);

	/**
	 * 获取指定id的调查对象. 注意使用这种方法获取的调查对象中的pages并没有加载, 所以不能使用
	 * @param id 调查id
	 * @return 指定id的调查对象
	 */
	Survey getSurvey(Integer id);

	/**
	 * 获取指定id的调查对象以及其所关联的页面对象
	 * @param id 调查id
	 * @return 指定id的调查对象及其关联的page
	 */
	Survey getSurveyAndPage(Integer id);

	/**
	 * 获取某个调查问卷的所有页面
	 * @param surveyId 调查问卷id
	 * @return 该调查问卷的所有页面
	 */
	List<Page> getPageBySurveyId(Integer surveyId);

	/**
	 * 创建一个调查问卷
	 * @param survey 调查问卷的基本信息
	 * @return 成功返回true, 否则返回false
	 */
	boolean createSurvey(Survey survey);

	/**
	 * 删除调查以及所关联页面和回答
	 *
	 * @param id 调查id
	 */
	void deleteSurvey(Integer id);

	/**
	 * 更新一个调查问卷
	 * @param survey 调查问卷
	 * @return 成功返回true, 否则返回false
	 */
	boolean updateSurvey(Survey survey);

}