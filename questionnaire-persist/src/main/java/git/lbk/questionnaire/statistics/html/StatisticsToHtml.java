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

package git.lbk.questionnaire.statistics.html;

import git.lbk.questionnaire.statistics.QuestionStatistics;
import git.lbk.questionnaire.statistics.SelectStatistics;

public interface StatisticsToHtml {

	void setQuestionStatistics(QuestionStatistics questionStatistics);

	String getHtmlString();

	/**
	 * @return 返回符合指定类型的QuestionStatistics对象
	 */
	static StatisticsToHtml getInstance(QuestionStatistics questionStatistics){
		StatisticsToHtml statisticsToHtml;
		if(questionStatistics instanceof SelectStatistics){
			statisticsToHtml = new SelectStatisticsToHtml();
		}
		else{
			throw new IllegalArgumentException("未知的参数类型");
		}
		statisticsToHtml.setQuestionStatistics(questionStatistics);
		return statisticsToHtml;
	}

}
