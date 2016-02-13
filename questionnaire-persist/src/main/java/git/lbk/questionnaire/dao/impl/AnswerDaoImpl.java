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

import git.lbk.questionnaire.entity.Answer;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("answerDao")
public class AnswerDaoImpl extends BaseDaoImpl<Answer> {

	/**
	 * 删除所有和surveyId关联的回答
	 * @param surveyId 调查id
	 */
	public void deleteBySurveyId(Integer surveyId){
		String hql = "delete Answer where survey.id = ?";
		updateEntityByHQL(hql, surveyId);
	}

	/**
	 * 获得所有和surveyId关联的回答
	 * @param surveyId 调查id
	 * @return 所有和surveyId关联的回答
	 */
	public List<Answer> getBySurveyId(Integer surveyId){
		String sql = "from Answer where survey.id = ?";
		return findEntityByHQL(sql, surveyId);
	}

}
