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

package git.lbk.questionnaire.statistics;

import git.lbk.questionnaire.answer.QuestionAnswer;
import git.lbk.questionnaire.entity.question.Question;
import git.lbk.questionnaire.util.ORMUtil;

/**
 * 实现了部分统计功能, 并增加了
 */
public abstract class AbstractQuestionStatistics<T extends Question> implements QuestionStatistics<T> {

	private T question;
	private Integer noneAnswerCount;
	private Integer answerPeopleCount;

	public AbstractQuestionStatistics(T question) {
		this.question = question;
		noneAnswerCount = 0;
		answerPeopleCount = 0;
	}

	/**
	 * 获得与之关联的问题
	 */
	@Override
	public T getQuestion() {
		return question;
	}

	@Override
	public Integer getAnswerPeopleCount() {
		return answerPeopleCount;
	}

	protected void incAnswerPeopleCount(){
		answerPeopleCount += 1;
	}

	@Override
	public boolean isRequired() {
		return question.isRequired();
	}

	protected void incNoneAnswerCount(){
		noneAnswerCount += 1;
	}

	/**
	 * 获得没有回答该题目的人数
	 *
	 * @return 没有回答该题目的人数
	 */
	@Override
	public Integer getNoneAnswer() {
		return noneAnswerCount;
	}

	/**
	 * 获得没有回答该题目的人数占总人数的百分比
	 *
	 * @return 没有回答该题目的人数占总人数的百分比
	 */
	@Override
	public Double getNoneAnswerPercentage() {
		if(answerPeopleCount == 0){
			return 0.0;
		}
		return noneAnswerCount.doubleValue() / answerPeopleCount;
	}

	/**
	 * 从QuestionAnswer实例获取用户回答的答案. 并添加到该对象的统计数据中
	 *
	 * @param questionAnswer 包含用户回答答案的QuestionAnswer实例
	 */
	public void addAnswerCount(QuestionAnswer questionAnswer){
		incAnswerPeopleCount();
		if(questionAnswer.isEmpty()){
			addNoneAnswer();
		}
		else {
			addAnswerMessage(questionAnswer);
		}
	}

	/**
	 * 将没有回答该题目的人数加一
	 */
	protected void addNoneAnswer() {
		if(!question.isRequired()) {
			incNoneAnswerCount();
		}
	}

	/**
	 * 从QuestionAnswer中获取回答信息, 并添加到统计信息中
	 */
	protected abstract void addAnswerMessage(QuestionAnswer questionAnswer);

	@Override
	public String toString() {
		return "AbstractQuestionStatistics{" +
				"question=" + ORMUtil.toString(question) +
				", noneAnswerCount=" + noneAnswerCount +
				", answerPeopleCount=" + answerPeopleCount +
				'}';
	}
}
