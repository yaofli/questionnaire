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

/**
 * 代表问题答案的抽象父类
 */
public abstract class AbstractQuestionAnswer<T extends Question> implements QuestionAnswer<T> {

	private int number;
	private T question;

	@Override
	public int getNumber() {
		return number;
	}

	/**
	 * 设置问题题号
	 * @param number 题号
	 */
	@Override
	public void setNumber(int number){
		this.number = number;
	}

	@Override
	public T getQuestion() {
		return question;
	}

	@Override
	public void setQuestion(T question){
		this.question = question;
	}

	/**
	 * 获得存储格式的题号和答案.
	 * @return 格式化后用户回答的答案和题号
	 */
	@Override
	public String getFormatNumberAndAnswer(){
		return ANSWER_START + number + ANSWER_EXCISION + getFormatAnswer();
	}

	public boolean isValidate(){
		if(!getQuestion().isRequired() && isEmpty()) {
			return true;
		}
		return validateRequiredAnswer();
	}

	/**
	 * 验证必须回答的问题的答案是否合法.
	 * @return 合法返回true, 否则返回false
	 */
	protected abstract boolean validateRequiredAnswer();

	@Override
	public String toString() {
		return "AbstractQuestionAnswer{" +
				"number=" + number +
				", question=" + question +
				'}';
	}

}
