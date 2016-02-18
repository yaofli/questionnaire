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

package git.lbk.questionnaire.service.impl;

import git.lbk.questionnaire.dao.impl.AnswerDaoImpl;
import git.lbk.questionnaire.dao.impl.PageDaoImpl;
import git.lbk.questionnaire.dao.impl.SurveyDaoImpl;
import git.lbk.questionnaire.entity.Answer;
import git.lbk.questionnaire.entity.Page;
import git.lbk.questionnaire.entity.Survey;
import git.lbk.questionnaire.answer.QuestionAnswer;
import git.lbk.questionnaire.answer.QuestionAnswerFactory;
import git.lbk.questionnaire.service.SurveyService;
import git.lbk.questionnaire.statistics.QuestionStatistics;
import git.lbk.questionnaire.statistics.QuestionStatisticsFactory;
import git.lbk.questionnaire.util.StringUtil;

import java.util.*;

public class SurveyServiceImpl implements SurveyService {

	private SurveyDaoImpl surveyDao;
	private PageDaoImpl pageDao;
	private AnswerDaoImpl answerDao;

	public void setSurveyDao(SurveyDaoImpl surveyDao) {
		this.surveyDao = surveyDao;
	}

	public void setPageDao(PageDaoImpl pageDao) {
		this.pageDao = pageDao;
	}

	public void setAnswerDao(AnswerDaoImpl answerDao) {
		this.answerDao = answerDao;
	}

	/**
	 * 获取某个用户的所有 正常状态 或者 设计状态 的调查问卷的基本信息
	 *
	 * @param userId 用户id
	 * @return 该用户的所有调查问卷
	 */
	@Override
	public List<Survey> getSurveyByUserId(Integer userId) {
		return surveyDao.getSurveyByUser(userId);
	}

	/**
	 * 获取指定id的 未删除 调查对象. 注意使用这种方法获取的调查对象中的pages并没有加载, 所以不能使用
	 *
	 * @param id 调查id
	 * @return 指定id的调查对象. 如果没有指定的调查对象 或者 调查对象为删除状态 则返回{@link Survey#INVALID_SURVEY}
	 */
	@Override
	public Survey getSurvey(Integer id) {
		Survey survey = surveyDao.getEntity(id);
		if(survey == null || survey.isDelete()) {
			return Survey.INVALID_SURVEY;
		}
		return survey;
	}

	/**
	 * 获取指定id的调查对象以及其所关联的页面对象.
	 *
	 * @param id 调查id
	 * @return 指定id的调查对象及其关联的page如果该调查对象为删除状态, 则返回{@link Survey#INVALID_SURVEY}
	 */
	@Override
	public Survey getSurveyAndPage(Integer id) {
		Survey survey = getSurvey(id);
		survey.getPages().size();   // 强制加载页面
		return survey;
	}

	/**
	 * 创建一个调查问卷
	 *
	 * @param survey 调查问卷的基本信息
	 * @return 成功返回true, 否则返回false
	 */
	@Override
	public boolean createSurvey(Survey survey) {
		surveyDao.saveEntity(survey);
		return true;
	}


	/**
	 * 将该调查的状态置为删除状态
	 *
	 * @param surveyId 调查id
	 * @param userId   进行操作的用户id
	 * @return 成功返回true, 否则返回false
	 */
	@Override
	public boolean deleteSurvey(Integer surveyId, Integer userId) {
		if(!surveyDao.surveyBelongUser(surveyId, userId)) {
			return false;
		}
		surveyDao.updateSurveyStatus(surveyId, Survey.DELETE_STATUS);
		return true;
	}

	/**
	 * 更新一个调查问卷
	 *
	 * @param survey 调查问卷
	 * @return 成功返回true, 否则返回false
	 */
	@Override
	public boolean updateSurvey(Survey survey) {
		if(!surveyDao.surveyBelongUser(survey.getId(), survey.getUserId())) {
			return false;
		}
		survey.setModifyTime(new Date());
		//fixme 注释的两行原本执行没有问题, 但是前几天执行却突然报错: object references an unsaved transient instance. 这是怎么回事?
		// surveyDao.updateEntity(survey);
		// pageDao.deletePageBySurveyId(survey.getId());
		pageDao.deletePageBySurveyId(survey.getId());
		surveyDao.updateEntity(survey);
		// 由于一般的调查问卷并没有太多的页面, 所以这里就不使用批量更新了, 直接一个一个的加进去
		for(Page page : survey.getPages()) {
			pageDao.saveEntity(page);
		}
		return true;
	}

	/**
	 * 反转调查设计状态
	 *
	 * @param surveyId 调查id
	 * @param userId   进行操作的用户id
	 * @return 成功返回true, 否则返回false
	 */
	@Override
	public boolean reverseDesigning(Integer surveyId, Integer userId) {
		Survey survey = surveyDao.getEntity(surveyId);
		if(!survey.getUserId().equals(userId) || survey.isDelete()) {
			return false;
		}
		survey.reverseDesign();
		surveyDao.updateSurveyStatus(surveyId, survey.getStatus());
		return true;
	}

	/**
	 * 保存用户提交的回答数据
	 *
	 * @param surveyId   调查id
	 * @param userAnswer 用户的回答数据
	 * @param ip         回答问题的用户的ip
	 * @return 如果答案合法则返回true, 并保存到数据库, 否则返回false
	 */
	public boolean saveAnswer(Integer surveyId, String userAnswer, String ip) {
		Survey survey = surveyDao.getEntity(surveyId);
		String answerStr = rigUpAnswerString(survey, userAnswer);
		if(StringUtil.isNull(answerStr)) {
			return false;
		}
		Answer answer = new Answer();
		answer.setAnswer(answerStr);
		answer.setAnswerTime(new Date());
		answer.setSurvey(survey);
		answer.setIp(ip);
		answerDao.saveEntity(answer);
		return true;
	}

	/**
	 * 配凑出存储的答案字符串
	 *
	 * @param survey     相应的调查对象
	 * @param userAnswer 用户回答的答案
	 * @return 如果所有的答案都合法, 则返回答案字符串. 否则返回空字符串
	 */
	private String rigUpAnswerString(Survey survey, String userAnswer) {
		List<QuestionAnswer> questionAnswers = QuestionAnswerFactory.createQuestionAnswers(survey, userAnswer);
		StringBuilder stringBuilder = new StringBuilder();
		for(QuestionAnswer questionAnswer : questionAnswers) {
			if(!questionAnswer.isValidate()) {
				return "";
			}
			stringBuilder.append(questionAnswer.getFormatNumberAndAnswer());
		}
		stringBuilder.append(QuestionAnswerFactory.getSentry(questionAnswers.size()));
		return stringBuilder.toString();
	}

	/**
	 * 获得指定调查对象的回答的统计信息
	 *
	 * @param surveyId 调查id
	 * @param userId   用户id, 用于权限判断
	 * @return 指定调查问卷的回答的统计信息
	 */
	@Override
	public List<? extends QuestionStatistics> getSurveyStatistics(Integer surveyId, Integer userId) {
		Survey survey = surveyDao.getEntity(surveyId);
		if(survey == null || !survey.getUserId().equals(userId)) {
			return Collections.emptyList();
		}
		List<Answer> answers = answerDao.getBySurveyId(surveyId);
		return QuestionStatisticsFactory.createSurveyStatisticsByAnswer(survey, answers);
	}

}
