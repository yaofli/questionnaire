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

import git.lbk.questionnaire.entity.question.MultiplySelectQuestion;
import git.lbk.questionnaire.entity.question.Question;

import java.util.*;
import java.util.stream.Collectors;

public class MultiplySelectionQuestAnswer extends AbstractQuestionAnswer {

	private Set<Integer> answer = new HashSet<>();

	@Override
	public void setAnswer(String answer){
		String[] answers = answer.split(",");
		for(int i=1; i<answers.length; i++){
			this.answer.add(Integer.valueOf(answers[i]));
		}
	}

	/**
	 * 获得用户回答的答案.
	 * @return 用户回答的答案.
	 */
	public Set<Integer> getAnswer(){
		return answer;
	}

	@Override
	public boolean isEmpty() {
		return answer.isEmpty();
	}

	@Override
	public String getFormatAnswer() {
		if(isEmpty()){
			return "";
		}
		return ',' + answer.stream().map(Object::toString).collect(Collectors.joining(",")) + ',';
	}

	@Override
	protected boolean validateRequiredAnswer() {
		Question question = getQuestion();
		if(!(question instanceof MultiplySelectQuestion)){
			return false;
		}
		MultiplySelectQuestion multiplySelectQuestion = (MultiplySelectQuestion) question;
		int questionNumber = multiplySelectQuestion.getOptions().size();
		return !answer.stream().anyMatch(sel -> sel<0 || sel>=questionNumber);
	}
}
