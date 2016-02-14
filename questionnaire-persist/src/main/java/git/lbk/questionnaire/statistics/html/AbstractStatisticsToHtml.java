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

public abstract class AbstractStatisticsToHtml implements StatisticsToHtml {

	private static final String TITLE_PLACEHOLDER = "${title}";
	private static final String CONTENT_PLACEHOLDER = "${content}";

	private static final String PANEL_TEMPLATE = "<div class=\"am-panel am-panel-default\">"
			+ "  <div class=\"am-panel-hd\">" + TITLE_PLACEHOLDER + "</div>"
			+ "  <div class=\"am-panel-bd\">" + CONTENT_PLACEHOLDER + "</div>"
			+ "</div>";


	private QuestionStatistics questionStatistics;

	@Override
	public void setQuestionStatistics(QuestionStatistics questionStatistics) {
		this.questionStatistics = questionStatistics;
	}

	public QuestionStatistics getQuestionStatistics() {
		return questionStatistics;
	}

	@Override
	public String getHtmlString(){
		return PANEL_TEMPLATE
				.replace(TITLE_PLACEHOLDER, questionStatistics.getQuestion().getTitle())
				.replace(CONTENT_PLACEHOLDER, getContent());
	}

	protected abstract String getContent();
}
