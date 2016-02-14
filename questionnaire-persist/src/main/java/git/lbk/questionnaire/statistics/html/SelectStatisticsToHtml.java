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

import git.lbk.questionnaire.entity.question.Option;
import git.lbk.questionnaire.statistics.QuestionStatistics;
import git.lbk.questionnaire.statistics.SelectStatistics;

import java.text.NumberFormat;
import java.util.*;

public class SelectStatisticsToHtml extends AbstractStatisticsToHtml {


	private static final String TRS_PLACEHOLDER = "${trs}";

	private static final String CONTENT_TEMPLATE =
			"<table class=\"am-table am-table-bordered am-table-striped am-table-compact am-table-hover\">" +
					"    <thead>" +
					"        <tr>" +
					"            <th>选项</th>" +
					"            <th>小计</th>" +
					"            <th>比例</th>" +
					"        </tr>" +
					"    </thead>" +
					"    <tbody>" + TRS_PLACEHOLDER + "</tbody>" +
					"</table>";

	private static final String OPTION_TITLE_PLACEHOLDER = "${option_title}";
	private static final String PEOPLE_NUMBER_PLACEHOLDER = "${people_number}";
	private static final String PERCENTAGE_PLACEHOLDER = "${percentage}";

	private static final String TR_TEMPLATE = "<tr>"
			+ "            <td>" + OPTION_TITLE_PLACEHOLDER + "</td>"
			+ "            <td>" + PEOPLE_NUMBER_PLACEHOLDER + "</td>"
			+ "            <td>" + PERCENTAGE_PLACEHOLDER + "</td>"
			+ "        </tr>";

	private static final NumberFormat PERCENT_FORMAT = NumberFormat.getPercentInstance();

	SelectStatistics selectStatistics;

	@Override
	protected String getContent() {
		selectStatistics = (SelectStatistics) getQuestionStatistics();
		return CONTENT_TEMPLATE.replace(TRS_PLACEHOLDER, getMainPart()+getNoneAnswerTr()+getSummary());
	}

	private String getMainPart(){
		List<Option> options = (selectStatistics.getQuestion()).getOptions();
		StringBuilder trsHtml = new StringBuilder();
		for(int i = 0; i < options.size(); i++) {
			trsHtml.append(TR_TEMPLATE
					.replace(OPTION_TITLE_PLACEHOLDER, options.get(i).getOption())
					.replace(PEOPLE_NUMBER_PLACEHOLDER, selectStatistics.getOptionAnswerCount(i).toString())
					.replace(PERCENTAGE_PLACEHOLDER, PERCENT_FORMAT.format(selectStatistics.getOptionAnswerPercent(i)))
			);
		}
		return trsHtml.toString();
	}

	private String getNoneAnswerTr() {
		QuestionStatistics statistics = getQuestionStatistics();
		if(statistics.isRequired()) {
			return "";
		}
		return TR_TEMPLATE.replace(OPTION_TITLE_PLACEHOLDER, "跳过此题")
				.replace(PEOPLE_NUMBER_PLACEHOLDER, statistics.getNoneAnswer().toString())
				.replace(PERCENTAGE_PLACEHOLDER,
						PERCENT_FORMAT.format(statistics.getNoneAnswerPercentage()));
	}

	private String getSummary(){
		QuestionStatistics statistics = getQuestionStatistics();
		return TR_TEMPLATE.replace(OPTION_TITLE_PLACEHOLDER, "本题有效填写人次")
				.replace(PEOPLE_NUMBER_PLACEHOLDER, statistics.getAnswerPeopleCount().toString())
				.replace(PERCENTAGE_PLACEHOLDER, "");
	}

}
