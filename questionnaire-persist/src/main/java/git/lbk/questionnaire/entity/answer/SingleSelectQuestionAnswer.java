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

package git.lbk.questionnaire.entity.answer;

import git.lbk.questionnaire.entity.question.Question;
import git.lbk.questionnaire.entity.question.SingleSelectQuestion;

public class SingleSelectQuestionAnswer extends AbstractQuestionAnswer {

	/**
	 * 空白答案
	 */
	public static final Integer EMPTY_ANSWER = -1;

	private Integer answer;

	@Override
	public void setAnswer(String answer) {
		try {
			this.answer = Integer.valueOf(answer);
		}
		catch(NumberFormatException e){
			this.answer = EMPTY_ANSWER;
		}
	}

	/**
	 * 返回用户回答的答案.
	 * @return 用户回答的答案. 如果答案无效, 则返回{@link #EMPTY_ANSWER}
	 */
	public Integer getAnswer(){
		return answer;
	}

	@Override
	public boolean isEmpty() {
		return EMPTY_ANSWER.equals(answer);
	}

	@Override
	public String getFormatAnswer() {
		return isEmpty() ? "" : answer.toString();
	}

	@Override
	protected boolean validateRequiredAnswer() {
		Question question = getQuestion();
		if(!(question instanceof SingleSelectQuestion)) {
			return false;
		}
		SingleSelectQuestion selectQuestion = (SingleSelectQuestion) question;
		int optionsNumber = selectQuestion.getOptions().size();
		return (answer >= 0 && answer < optionsNumber);
	}
}
