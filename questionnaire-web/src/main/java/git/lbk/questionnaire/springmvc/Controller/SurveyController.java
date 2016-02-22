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

package git.lbk.questionnaire.springmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import git.lbk.questionnaire.entity.Page;
import git.lbk.questionnaire.entity.Survey;
import git.lbk.questionnaire.query.SurveyCondition;
import git.lbk.questionnaire.service.SurveyService;
import git.lbk.questionnaire.util.NetUtil;
import git.lbk.questionnaire.util.annotation.CheckToken;
import git.lbk.questionnaire.util.annotation.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@SessionAttributes(value = {UserController.SESSION_USER_ID}, types = {Integer.class})
@RequestMapping("/survey")
@Controller
public class SurveyController {

	private static final Logger logger = LoggerFactory.getLogger(SurveyController.class);

	@Autowired
	private SurveyService surveyService;

	@RequestMapping(value = "/mySurvey", method = RequestMethod.GET)
	public String mySurvey(Map<String, Object> map, SurveyCondition surveyCondition) {
		surveyCondition.setUserId((Integer) map.get(UserController.SESSION_USER_ID));
		map.put("condition", surveyCondition);
		map.put("page", surveyService.findSurvey(surveyCondition));
		return "mySurvey";
	}

	@CheckToken(returnInfo = @ErrorHandler(returnValue = "redirect:/survey/mySurvey"))
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String addSurvey(@Valid Survey survey,
	                        @ModelAttribute(UserController.SESSION_USER_ID) Integer userId) throws IOException {
		survey.setStatus(Survey.DESIGN_STATUS);
		survey.setUserId(userId);
		if(!surveyService.createSurvey(survey)) {
			return "redirect:/error";
		}
		return "redirect:/survey/design/" + survey.getId();
	}

	/**
	 * 将指定调查的状态置为删除状态
	 *
	 * @param id     调查id
	 * @param userId 操作用户id(从session中获取)
	 * @return 成功返回true, 否则返回false
	 */
	@ResponseBody
	@RequestMapping(value = "/{surveyId}", method = RequestMethod.DELETE)
	public boolean deleteSurvey(@PathVariable(value = "surveyId") Integer id,
	                            @ModelAttribute(UserController.SESSION_USER_ID) Integer userId) {
		return surveyService.deleteSurvey(id, userId);
	}

	/**
	 * 更新调查对象. 为了保证url的一致性, 添加了冗余的路径surveyId
	 *
	 * @param surveyStr 调查对象数据
	 * @return 成功返回success
	 */
	@ResponseBody
	@RequestMapping(value = "/{surveyId}", method = RequestMethod.PUT)
	public String updateSurvey(@RequestParam("survey") String surveyStr,
	                           @ModelAttribute(UserController.SESSION_USER_ID) Integer userId) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Survey survey = objectMapper.readValue(surveyStr, Survey.class);
			survey.setUserId(userId);
			survey.setStatus(Survey.DESIGN_STATUS);
			for(Page page : survey.getPages()) {
				page.setSurvey(survey);
			}
			surveyService.updateSurvey(survey);
		}
		catch(IOException e) {
			logger.warn("string to json error", e.getMessage());
			return "fail";
		}
		return "success";
	}

	/**
	 * 获得调查对象及其关联的页面对象
	 *
	 * @param id     调查id
	 * @param userId 登录的用户id
	 * @return 如果发送请求的用户(从session中获取)和该调查对象的所有者不是同一个人, 且该调查处于设计状态, 则返回{@link Survey#INVALID_SURVEY}
	 */
	@ResponseBody
	@RequestMapping(value = "/{surveyId}", method = RequestMethod.GET)
	public Survey getSurvey(@PathVariable("surveyId") Integer id,
	                        @ModelAttribute(UserController.SESSION_USER_ID) Integer userId) {
		Survey survey = surveyService.getSurveyAndPage(id);
		if(!survey.getUserId().equals(userId) && !survey.isNormal()) {
			return Survey.INVALID_SURVEY;
		}
		return survey;
	}

	@ResponseBody
	@RequestMapping(value = "/reverseDesigning/{surveyId}", method = RequestMethod.PUT)
	public boolean reverseSurveyDesigning(@PathVariable("surveyId") Integer id,
	                                      @ModelAttribute(UserController.SESSION_USER_ID) Integer userId) {
		return surveyService.reverseDesigning(id, userId);
	}

	@RequestMapping(value = "/design/{surveyId}", method = RequestMethod.GET)
	public String toDesignSurveyPage(@PathVariable("surveyId") Integer id,
	                                 Map<String, Object> map,
	                                 @ModelAttribute(UserController.SESSION_USER_ID) Integer userId) {
		Survey survey = surveyService.getSurveyAndPage(id);
		if(survey == null) {
			return "/404";
		}
		if(!survey.getUserId().equals(userId)) {
			return "redirect:/index";
		}
		map.put("survey", survey.toJson());
		return "designSurvey";
	}

	@RequestMapping(value="/surveySquare")
	public String surveySquare(Map<String, Object> map, SurveyCondition surveyCondition){
		surveyCondition.setUserId(null);
		surveyCondition.setStatus(Survey.NORMAL_STATUS);
		map.put("condition", surveyCondition);
		map.put("page", surveyService.findSurvey(surveyCondition));
		return "surveySquare";
	}

	@RequestMapping(value = "/participate/{surveyId}")
	public String toParticipateSurveyPage(@PathVariable("surveyId") Integer id, Map<String, Object> map) {
		Survey survey = surveyService.getSurveyAndPage(id);
		if(survey.isDesign()) {
			return "/404";
		}
		map.put("survey", survey);
		return "participateSurvey";
	}

	@ResponseBody
	@RequestMapping(value = "/commitAnswer/{surveyId}", method = RequestMethod.POST)
	public boolean participateSurvey(@PathVariable("surveyId") Integer id, @RequestParam("answer") String answer,
	                                 HttpServletRequest request) {
		return surveyService.saveAnswer(id, answer, NetUtil.getRealIP(request));
	}

	@RequestMapping(value = "/statistics/{surveyId}")
	public String answerStatistics(@PathVariable("surveyId") Integer surveyId,
	                               Map<String, Object> map,
	                               @ModelAttribute(UserController.SESSION_USER_ID) Integer userId) {
		map.put("statistics", surveyService.getSurveyStatistics(surveyId, userId));
		return "answerStatistics";
	}

}
