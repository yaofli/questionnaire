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

package git.lbk.questionnaire.answer;

import git.lbk.questionnaire.entity.question.Question;

public interface QuestionAnswer <T extends Question> {
	/**
	 * 回答中新题目开始的标记
	 */
	String ANSWER_START = "\u001E";
	/**
	 * 回答中回答内容开始的标记
	 */
	String ANSWER_EXCISION = "\u001F";

	int getNumber();

	void setNumber(int number);

	T getQuestion();

	void setQuestion(T question);

	/**
	 * 设置用户的回答
	 * @param answer 用户的回答
	 */
	void setAnswer(String answer);

	/**
	 * 判断答案是否为空
	 * @return 为空返回true, 否则返回false
	 */
	boolean isEmpty();

	/**
	 * 获得经过格式化的用户回答的答案和题号
	 * @return 经过格式化的用户回答的答案和题号
	 */
	String getFormatNumberAndAnswer();

	/**
	 * 获得经过格式化的用户回答的答案
	 * @return 格式化后用户回答的答案
	 */
	String getFormatAnswer();

	/**
	 * 验证答案是否合法
	 * @return 合法返回true, 否则返回false
	 */
	boolean isValidate();

}
