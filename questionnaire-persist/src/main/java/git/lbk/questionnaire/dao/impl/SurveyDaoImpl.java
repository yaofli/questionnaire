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
	 * 根据用户id获取所有的调查问卷
	 * @param userId 用户id
	 * @return 该用户所有的调查问卷
	 */
	public List<Survey> getSurveyByUser(Integer userId){
		String hql = "from Survey s where s.user.id=?";
		return findEntityByHQL(hql, userId);
	}

}
