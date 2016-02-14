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

package git.lbk.questionnaire.tag;

import git.lbk.questionnaire.statistics.QuestionStatistics;
import git.lbk.questionnaire.statistics.html.StatisticsToHtml;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.*;

public class QuestionStatisticsTableTag extends SimpleTagSupport {

	private List<QuestionStatistics> statistics;

	public List<QuestionStatistics> getStatistics() {
		return statistics;
	}

	public void setStatistics(List<QuestionStatistics> questionStatisticses) {
		this.statistics = questionStatisticses;
	}

	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		out.print("<div class=\"statistics am-panel-group\">");
		if(statistics.isEmpty() || statistics.get(0).getAnswerPeopleCount()==0){
			out.println("<p class=\"am-text-center am-text-xl\">暂没有人回答问卷, 请过段时间再查看</p>");
		}
		else {
			for(QuestionStatistics statistic : statistics) {
				out.print(StatisticsToHtml.getInstance(statistic).getHtmlString());
			}
		}
		out.print("</div>");
	}

}
