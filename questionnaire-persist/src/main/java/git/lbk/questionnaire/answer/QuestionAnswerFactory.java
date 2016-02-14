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

import git.lbk.questionnaire.entity.Page;
import git.lbk.questionnaire.entity.Survey;
import git.lbk.questionnaire.entity.question.MultiplySelectQuestion;
import git.lbk.questionnaire.entity.question.Question;
import git.lbk.questionnaire.entity.question.SingleSelectQuestion;
import git.lbk.questionnaire.util.StringUtil;

import java.util.*;

public class QuestionAnswerFactory {


	/**
	 * 获得哨兵答案
	 *
	 * @param maxQuestionNumber 问卷最终最大的题号
	 * @return 哨兵答案
	 */
	public static String getSentry(int maxQuestionNumber) {
		return QuestionAnswer.ANSWER_START + (maxQuestionNumber + 1);
	}


	/**
	 * 根据问题类型, 获得对应类型的QuestionAnswer对象
	 *
	 * @param question Question对象
	 * @return 对应的QuestionAnswer对象
	 * @throws IllegalArgumentException 如果参数详细类型未知
	 */
	public static <T extends Question> QuestionAnswer<T> createQuestionAnswer(T question) throws IllegalArgumentException {
		QuestionAnswer<T> questionAnswer;
		if(question instanceof SingleSelectQuestion) {
			questionAnswer = (QuestionAnswer<T>) new SingleSelectQuestionAnswer();
		}
		else if(question instanceof MultiplySelectQuestion) {
			questionAnswer = (QuestionAnswer<T>) new MultiplySelectionQuestAnswer();
		}
		else {
			throw new IllegalArgumentException("未知的参数类型");
		}
		questionAnswer.setQuestion(question);
		return questionAnswer;
	}

	/**
	 * 根据用户的回答 和 调查问卷本身创建所有问题的QuestionAnswer
	 * @param survey 调查问卷本身
	 * @param answer 用户的回答
	 * @return 所有问题的QuestionAnswer列表, 按照题号排序
	 */
	public static List<QuestionAnswer> createQuestionAnswers(Survey survey, String answer) {
		List<QuestionAnswer> questionAnswerList = new ArrayList<>();
		Map<Integer, String> splitAnswer = splitAnswer(answer);
		int questionNumber = 1;
		for(Page page : survey.getPages()){
			for(Question question : page.getQuestions()) {
				QuestionAnswer questionAnswer = createQuestionAnswer(question);
				questionAnswer.setNumber(questionNumber);
				questionAnswer.setAnswer(splitAnswer.get(questionNumber));
				questionAnswerList.add(questionAnswer);
				questionNumber += 1;
			}
		}
		return questionAnswerList;
	}

	/**
	 * 将答案字符串转化成map, 以题号作为键, 答案为值
	 * @param userAnswer 用户回答的字符串
	 * @return 转化后的map
	 * @exception NumberFormatException 如果其中某些题号无法转换成整数
	 */
	public static Map<Integer, String> splitAnswer(String userAnswer) throws NumberFormatException{
		Map<Integer, String> answerMap = new HashMap<>();
		String[] answers = userAnswer.split(QuestionAnswer.ANSWER_START);
		for(String answer : answers){
			if(StringUtil.isNull(answer)){
				continue;
			}
			String[] answerSplit = answer.split(QuestionAnswer.ANSWER_EXCISION);
			if(answerSplit.length == 1 ){
				answerMap.put(Integer.valueOf(answerSplit[0]), null);
			}
			else if(answerSplit.length == 2){
				answerMap.put(Integer.valueOf(answerSplit[0]), answerSplit[1]);
			}
		}
		return answerMap;
	}

}
