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
import git.lbk.questionnaire.entity.Page;
import git.lbk.questionnaire.entity.Survey;
import git.lbk.questionnaire.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SurveyServiceImpl implements SurveyService {

	private final static Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

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
	 * 获取某个用户的所有调查问卷的基本信息
	 *
	 * @param userId 用户id
	 * @return 该用户的所有调查问卷
	 */
	@Override
	public List<Survey> getSurveyByUserId(Integer userId){
		try {
			return surveyDao.getNormalSurveyByUser(userId);
		}
		catch(RuntimeException ex){
			logger.error("获取用户所有调查问卷时发生错误! userId: " + userId, ex);
		}
		return new ArrayList<>(0);
	}

	/**
	 * 获取指定id的调查对象. 注意使用这种方法获取的调查对象中的pages并没有加载, 所以不能使用
	 *
	 * @param id 调查id
	 * @return 指定id的调查对象
	 */
	@Override
	public Survey getSurvey(Integer id){
		try{
			return surveyDao.getEntity(id);
		}
		catch(RuntimeException ex){
			logger.error("获取调查对象时发生错误! surveyId: " +id, ex);
		}
		return null;
	}

	/**
	 * 获取指定id的调查对象以及其所关联的页面对象. 如果该调查对象为删除状态, 则返回null
	 *
	 * @param id 调查id
	 * @return 指定id的调查对象及其关联的page
	 */
	@Override
	public Survey getNormalSurveyAndPage(Integer id){
		Survey survey = null;
		try {
			survey = surveyDao.getEntity(id);
			if(!survey.getStatus().equals(Survey.NORMAL_STATUS)){
				return Survey.INVALID_SURVEY;
			}
			survey.getPages().size();
		}
		catch(Exception ex) {
			logger.error("获取调查问卷对象时发生错误: surveyId: " + id, ex);
		}
		return survey;
	}

	/**
	 * 获取某个调查问卷的所有页面
	 *
	 * @param surveyId 调查问卷id
	 * @return 该调查问卷的所有页面
	 */
	@Override
	public List<Page> getPageBySurveyId(Integer surveyId){
		try{
			return pageDao.getPagesBySurveyId(surveyId);
		}
		catch(RuntimeException ex){
			logger.error("获取调查问卷的所有页面时发生错误! surveyId: " + surveyId, ex);
		}
		return new ArrayList<>(0);
	}

	/**
	 * 创建一个调查问卷
	 *
	 * @param survey 调查问卷的基本信息
	 * @return 成功返回true, 否则返回false
	 */
	@Override
	public boolean createSurvey(Survey survey){
		try {
			surveyDao.saveEntity(survey);
			return true;
		}
		catch(Exception e) {
			logger.error("新建调查时发生错误" , e);
		}
		return false;
	}


	/**
	 * 删除调查以及所关联页面和回答
	 *
	 * @param surveyId     调查id
	 * @param userId 进行操作的用户id
	 * @return 成功返回true, 否则返回false
	 */
	@Override
	public boolean deleteSurvey(Integer surveyId, Integer userId){
		try {
			if(!surveyDao.surveyBelongUser(surveyId, userId)){
				return false;
			}
			surveyDao.updateSurveyStatus(surveyId, Survey.DELETE_STATUS);
			return true;
		}
		catch(Exception e) {
			logger.error("新建调查时发生错误", e);
		}
		return false;
	}

	/**
	 * 更新一个调查问卷
	 *
	 * @param survey 调查问卷
	 * @return 成功返回true, 否则返回false
	 */
	@Override
	public boolean updateSurvey(Survey survey){
		try {
			if(!surveyDao.surveyBelongUser(survey.getId(), survey.getUserId())){
				return false;
			}
			survey.setModifyTime(new Date());
			surveyDao.updateEntity(survey);
			pageDao.deletePageBySurveyId(survey.getId());
			// 由于一般的调查问卷并没有太多的页面, 所以这里就不使用批量更新了, 直接一个一个的加进去
			for(Page page : survey.getPages()) {
				pageDao.saveEntity(page);
			}
			return true;
		}
		catch(Exception e) {
			logger.error("更新调查时发生错误", e);
		}
		return false;
	}

	/**
	 * 反转调查设计状态
	 *
	 * @param surveyId 调查id
	 * @param userId   进行操作的用户id
	 * @return 成功返回true, 否则返回false
	 */
	@Override
	public boolean reverseDesigning(Integer surveyId, Integer userId){
		try {
			Survey survey = surveyDao.getEntity(surveyId);
			if(!survey.getUserId().equals(userId) || !survey.getStatus().equals(Survey.NORMAL_STATUS)){
				return false;
			}
			survey.setDesigning(!survey.getDesigning());
			surveyDao.updateEntity(survey);
			return true;
		}
		catch(Exception e) {
			logger.error("更新调查开放状态时发生错误", e);
		}
		return false;
	}

}
