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

package git.lbk.questionnaire.dao.impl;

import git.lbk.questionnaire.entity.Survey;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("surveyDao")
public class SurveyDaoImpl extends BaseDaoImpl<Survey> {

	/**
	 * 根据用户id获取所有 正常状态 或者 设计状态 的调查问卷
	 * @param userId 用户id
	 * @return 该用户所有的调查问卷
	 */
	public List<Survey> getSurveyByUser(Integer userId){
		String hql = "from Survey s where s.userId=? and (s.status=? or s.status=?)";
		return findEntityByHQL(hql, userId, Survey.NORMAL_STATUS, Survey.DESIGN_STATUS);
	}

	/**
	 * 判断一个调查问卷是否属于一个用户的
	 * @param surveyId 调查问卷id
	 * @param userId 用户id
	 * @return 如果调查问卷属于指定用户则返回true, 否则返回false
	 */
	public boolean surveyBelongUser(int surveyId, int userId){
		String hql = "select userId from Survey s where s.id=?";
		return  (int)uniqueResult(hql, surveyId) == userId;
	}

	/**
	 * 将调查状态置
	 * @param id 调查id
	 * @param status 最新的状态
	 */
	public void updateSurveyStatus(Integer id, Integer status){
		String hql = "update Survey set status = ?, modifyTime = CURRENT_TIME() where id = ?";
		updateEntityByHQL(hql, status, id);
	}

}
