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

import git.lbk.questionnaire.entity.Survey;
import git.lbk.questionnaire.query.Page;
import git.lbk.questionnaire.query.SurveyCondition;
import git.lbk.questionnaire.statistics.QuestionStatistics;

import java.util.*;


public interface SurveyService {

	/**
	 * 查询出符合条件的survey对象
	 *
	 * @param surveyCondition 查询条件以及分页条件
	 * @return 该页的survey信息
	 */
	Page<Survey> findSurvey(SurveyCondition surveyCondition);

	/**
	 * 获取指定id的调查对象. 注意使用这种方法获取的调查对象中的pages并没有加载, 所以不能使用
	 * @param id 调查id
	 * @return 指定id的调查对象
	 */
	Survey findSurvey(Integer id);

	/**
	 * 获取指定id的调查对象以及其所关联的页面对象. 如果该调查对象为删除状态, 则返回null
	 * @param id 调查id
	 * @return 指定id的调查对象及其关联的page
	 */
	Survey getSurveyAndPage(Integer id);

	/**
	 * 创建一个调查问卷
	 * @param survey 调查问卷的基本信息
	 * @return 成功返回true, 否则返回false
	 */
	boolean createSurvey(Survey survey);

	/**
	 * 删除调查以及所关联页面和回答
	 *
	 * @param surveyId 调查id
	 * @param userId 进行操作的用户id
	 * @return 成功返回true, 否则返回false
	 */
	boolean deleteSurvey(Integer surveyId, Integer userId);

	/**
	 * 更新一个调查问卷
	 * @param survey 调查问卷
	 * @return 成功返回true, 否则返回false
	 */
	boolean updateSurvey(Survey survey);

	/**
	 * 反转调查设计状态
	 * @param surveyId 调查id
	 * @param userId 进行操作的用户id
	 * @return 成功返回true, 否则返回false
	 */
	boolean reverseDesigning(Integer surveyId, Integer userId);

	/**
	 * 保存用户提交的回答数据
	 *
	 * @param surveyId   调查id
	 * @param userAnswer 用户的回答数据
	 * @param ip         回答问题的用户的ip
	 * @return 如果答案合法则返回true, 并保存到数据库, 否则返回false
	 */
	boolean saveAnswer(Integer surveyId, String userAnswer, String ip);

	/**
	 * 获得指定调查对象的回答的统计信息
	 * @param surveyId 调查id
	 * @param userId 用户id, 用于权限判断
	 * @return 指定调查问卷的回答的统计信息
	 */
	List<? extends QuestionStatistics> getSurveyStatistics(Integer surveyId, Integer userId);

}
