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

import git.lbk.questionnaire.util.ORMUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 问卷调查答题记录
 */
public class Answer implements Serializable{
	private static final long serialVersionUID = -8456675848535563837L;

	private Integer id;
	private Survey survey;
	private Date answerTime;
	private String ip;
	private String answer;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Date getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(Date answerTime) {
		this.answerTime = answerTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Answer answer1 = (Answer) o;

		if(id != null ? !id.equals(answer1.id) : answer1.id != null) return false;
		if(ip != null ? !ip.equals(answer1.ip) : answer1.ip != null) return false;
		return !(answer != null ? !answer.equals(answer1.answer) : answer1.answer != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (ip != null ? ip.hashCode() : 0);
		result = 31 * result + (answer != null ? answer.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Answer{" +
				"id=" + id +
				", survey=" + ORMUtil.toString(survey) +
				", answerTime=" + answerTime +
				", ip='" + ip + '\'' +
				", answer='" + answer + '\'' +
				'}';
	}
}
