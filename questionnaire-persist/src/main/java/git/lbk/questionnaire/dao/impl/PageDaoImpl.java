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

import git.lbk.questionnaire.entity.Page;
import org.springframework.stereotype.Repository;

@Repository("pageDao")
public class PageDaoImpl extends BaseDaoImpl<Page> {

	/**
	 * 删除一个调查问卷的所有页面
	 * @param surveyId 调查问卷id
	 */
	public void deletePageBySurveyId(Integer surveyId){
		String hql = "delete Page p where p.survey.id=?";
		updateEntityByHQL(hql, surveyId);
	}

}
