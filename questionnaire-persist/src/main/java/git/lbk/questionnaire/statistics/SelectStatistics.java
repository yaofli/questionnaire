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

import git.lbk.questionnaire.answer.MultiplySelectionQuestAnswer;
import git.lbk.questionnaire.answer.QuestionAnswer;
import git.lbk.questionnaire.answer.SingleSelectQuestionAnswer;
import git.lbk.questionnaire.entity.question.SelectQuestion;

import java.util.*;

public class SelectStatistics extends AbstractQuestionStatistics<SelectQuestion> {

	private List<Integer> answerCounts;
	private int optionNumber;

	public SelectStatistics(SelectQuestion question) {
		super(question);
		optionNumber = question.getOptions().size();
		answerCounts = new ArrayList<>(optionNumber);
		for(int i = 0; i <= optionNumber; i++) {
			answerCounts.add(0);
		}
	}

	/**
	 * 获取每个选项选择的人数
	 *
	 * @return 每个选项选择的人数
	 */
	public List<Integer> getAnswerCounts() {
		return answerCounts;
	}

	/**
	 * 获得指定选项回答的人数
	 * @param index 选项的索引, 从0开始
	 * @return 选择指定选项的人数
	 */
	public Integer getOptionAnswerCount(int index){
		return answerCounts.get(index);
	}

	/**
	 * 获得指定选项回答的人数的百分比
	 * @param index 选项的索引, 从0开始
	 * @return 选择指定选项的人的百分比.
	 */
	public Double getOptionAnswerPercent(int index){
		if(getAnswerPeopleCount().equals(0)){
			return 0.0;
		}
		return answerCounts.get(index) / getAnswerPeopleCount().doubleValue();
	}

	/**
	 * 将选择指定选项的人数加一
	 *
	 * @param answerNumber 问题的题号, 从0开始
	 */
	public void addAnswerCount(int answerNumber) {
		Integer number = answerCounts.get(answerNumber);
		answerCounts.set(answerNumber, number + 1);
	}

	/**
	 * 将迭代器中所有选项的选择人数加一
	 *
	 * @param answerNumbers 问题的题号迭代器. 题号从0开始
	 */
	public void addAnswerCount(Iterable<? extends Number> answerNumbers) {
		for(Number answerNumber : answerNumbers) {
			addAnswerCount(answerNumber.intValue());
		}
	}

	/**
	 * 从QuestionAnswer实例获取用户回答的答案. 并添加到该对象的统计数据中
	 *
	 * @param questionAnswer 包含用户回答答案的QuestionAnswer实例
	 */
	@Override
	protected void addAnswerMessage(QuestionAnswer questionAnswer) {
		if(questionAnswer instanceof SingleSelectQuestionAnswer){
			addAnswerCount((SingleSelectQuestionAnswer)questionAnswer);
		}
		else if(questionAnswer instanceof MultiplySelectionQuestAnswer){
			addAnswerCount((MultiplySelectionQuestAnswer) questionAnswer);
		}
	}

	/**
	 * 从SingleSelectQuestionAnswer实例获取用户选择的选项, 将指定选项的人数加一
	 *
	 * @param ssqa SingleSelectQuestionAnswer实例
	 */
	public void addAnswerCount(SingleSelectQuestionAnswer ssqa) {
		if(!SingleSelectQuestionAnswer.EMPTY_ANSWER.equals(ssqa.getAnswer())) {
			addAnswerCount(ssqa.getAnswer());
		}
	}

	/**
	 * 从MultiplySelectQuestionAnswer实例中获取用户选择的选项, 并将指定选项的人数加一
	 *
	 * @param msqa MultiplySelectQuestionAnswer实例
	 */
	public void addAnswerCount(MultiplySelectionQuestAnswer msqa) {
		addAnswerCount(msqa.getAnswer());
	}

	@Override
	public String toString() {
		return "SelectStatistics{" +
				"answerCounts=" + answerCounts +
				", optionNumber=" + optionNumber +
				"} " + super.toString();
	}
}
