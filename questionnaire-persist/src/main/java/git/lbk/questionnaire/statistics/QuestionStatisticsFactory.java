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

import git.lbk.questionnaire.entity.Answer;
import git.lbk.questionnaire.entity.Page;
import git.lbk.questionnaire.entity.Survey;
import git.lbk.questionnaire.entity.answer.QuestionAnswer;
import git.lbk.questionnaire.entity.answer.QuestionAnswerFactory;
import git.lbk.questionnaire.entity.question.MultiplySelectQuestion;
import git.lbk.questionnaire.entity.question.Question;
import git.lbk.questionnaire.entity.question.SelectQuestion;
import git.lbk.questionnaire.entity.question.SingleSelectQuestion;

import java.util.*;

public class QuestionStatisticsFactory {

	/**
	 * 根据问题类型, 获得对应类型的QuestionStatistics对象
	 *
	 * @param question Question对象
	 * @return 对应的QuestionStatistics对象
	 * @throws IllegalArgumentException 如果参数详细类型未知
	 */
	public static QuestionStatistics createQuestionStatistics(Question question){
		if(question instanceof SingleSelectQuestion || question instanceof MultiplySelectQuestion){
			return new SelectStatistics((SelectQuestion) question);
		}
		throw new IllegalArgumentException("未知的参数类型");
	}

	/**
	 * 创建一个调查的所有统计数据
	 * @param survey 调查对象
	 * @param answers 需要统计的用户回答的答案. 每一项为一个完整的答案
	 * @return 一个调查的所有统计数据. 按题号排序
	 */
	public static List<? extends QuestionStatistics> createSurveyStatistics(Survey survey, List<String> answers){
		List<QuestionStatisticsAbstract> questionStatisticsList = new ArrayList<>();
		for(Page page : survey.getPages()) {
			for(Question question : page.getQuestions()) {
				QuestionStatisticsAbstract questionStatistics = (QuestionStatisticsAbstract) createQuestionStatistics(question);
				questionStatisticsList.add(questionStatistics);
			}
		}
		for(String answer : answers){
			List<QuestionAnswer> questionAnswers = QuestionAnswerFactory.createQuestionAnswers(survey, answer);
			for(int i=0; i<questionStatisticsList.size(); i++){
				questionStatisticsList.get(i).addAnswerCount(questionAnswers.get(i));
			}
		}
		return questionStatisticsList;
	}

	/**
	 * 创建一个调查的所有统计数据
	 *
	 * @param survey  调查对象
	 * @param answers 需要统计的用户回答的Answer实体
	 * @return 一个调查的所有统计数据. 按题号排序
	 */
	public static List<? extends QuestionStatistics> createSurveyStatisticsByAnswer(Survey survey, List<Answer> answers) {
		List<String> answerStringList = new ArrayList<>();
		for(Answer answer : answers) {
			answerStringList.add(answer.getAnswer());
		}
		return createSurveyStatistics(survey, answerStringList);
	}

}
