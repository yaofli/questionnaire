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

import git.lbk.questionnaire.entity.question.Question;

/**
 * 问题统计的抽象模型
 */
public interface QuestionStatistics<T extends Question> {

	/**
	 * 获得与之关联的问题
	 */
	T getQuestion();

	/**
	 * 获得参与回答的人数
	 * @return 参与回答的人数
	 */
	Integer getAnswerPeopleCount();

	/**
	 * 与该统计对象关联的问题是否是必答的.
	 */
	boolean isRequired();

	/**
	 * 获得没有回答该题目的人数
	 *
	 * @return 没有回答该题目的人数
	 */
	Integer getNoneAnswer();

	/**
	 * 获得没有回答该题目的人数占总人数的百分比
	 *
	 * @return 没有回答该题目的人数占总人数的百分比
	 */
	Double getNoneAnswerPercentage();

}
