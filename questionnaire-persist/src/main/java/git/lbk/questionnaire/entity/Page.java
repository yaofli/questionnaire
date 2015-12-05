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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import git.lbk.questionnaire.entity.question.Question;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

/**
 * 问卷调查的一个页面
 */
public class Page {

	private Integer id;
	@NotNull
	private String title;
	private List<Question> questions;
	private Integer rank;
	private Survey survey;

	public Page() {
	}

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

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public String getQuestionsStr() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(questions);
		}
		catch(JsonProcessingException e) {}
		return "[]";
	}

	public void setQuestionsStr(String question) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			this.questions = objectMapper.readValue(question, objectMapper.getTypeFactory().constructCollectionType(List.class, Question.class));
		}
		catch(IOException e) {
			this.questions = new ArrayList<>(0);
		}
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
		if(questions != null ? !questions.equals(page.questions) : page.questions != null) return false;
		return !(rank != null ? !rank.equals(page.rank) : page.rank != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (questions != null ? questions.hashCode() : 0);
		result = 31 * result + (rank != null ? rank.hashCode() : 0);
		return result;
	}

	@Override
	public String
	toString() {
		return "Page{" +
				"id=" + id +
				", title='" + title + '\'' +
				", question='" + questions + '\'' +
				", rank=" + rank +
				'}';
	}
}
