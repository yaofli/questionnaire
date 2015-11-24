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

package git.lbk.questionnaire.entity;

/**
 * 问卷调查的一个页面
 */
public class Page {

	/**
	 * 页面信息和问题的分割符.
	 */
	public static final String QUESTION_START= "\u0002";

	/**
	 * 问题之间的分割符
	 */
	public static final String QUESTION_EXCISION = "\u001D";

	/**
	 * 页面信息以及问题中各个模块之间的分割符
	 */
	public static final String QUESTION_MODULE = "\u001E";

	/**
	 * 模块中的子项目的分割符
	 */
	public static final String MODULE_EXCISION ="\u001F";

	private Integer id;
	private String title;
	private String question;
	private Integer questionCount;
	private Integer questionStartId;
	private Integer rank;
	private Survey survey;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public Integer getQuestionStartId() {
		return questionStartId;
	}

	public void setQuestionStartId(Integer questionStartId) {
		this.questionStartId = questionStartId;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Page page = (Page) o;

		if(id != null ? !id.equals(page.id) : page.id != null) return false;
		if(title != null ? !title.equals(page.title) : page.title != null) return false;
		if(question != null ? !question.equals(page.question) : page.question != null) return false;
		if(questionCount != null ? !questionCount.equals(page.questionCount) : page.questionCount != null) return
				false;
		if(questionStartId != null ? !questionStartId.equals(page.questionStartId) : page.questionStartId != null)
			return false;
		if(rank != null ? !rank.equals(page.rank) : page.rank != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (question != null ? question.hashCode() : 0);
		result = 31 * result + (questionCount != null ? questionCount.hashCode() : 0);
		result = 31 * result + (questionStartId != null ? questionStartId.hashCode() : 0);
		result = 31 * result + (rank != null ? rank.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Page{" +
				"rank=" + rank +
				", questionStartId=" + questionStartId +
				", questionCount=" + questionCount +
				", question='" + question + '\'' +
				", title='" + title + '\'' +
				", id=" + id +
				'}';
	}
}
