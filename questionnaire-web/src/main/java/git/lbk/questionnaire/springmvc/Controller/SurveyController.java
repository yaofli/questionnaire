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

import git.lbk.questionnaire.entity.Survey;
import git.lbk.questionnaire.service.SurveyService;
import git.lbk.questionnaire.util.annotation.CheckToken;
import git.lbk.questionnaire.util.annotation.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

	@ModelAttribute
	public void surveyModel(@RequestParam(value = "id", required = false) Integer id,
	                        Map<String, Object> map) {
		if(id != null) {
			Survey survey = surveyService.getSurveyAndPage(id);
			map.put("survey", survey);
		}
	}

	@RequestMapping("/mySurvey")
	public String mySurvey(Map<String, Object> map, @ModelAttribute(UserController.SESSION_USER_ID) Integer userId) {
		map.put("surveyList", surveyService.getSurveyByUserId(userId));
		return "mySurvey";
	}

	//fixme 其实这里原本是直接返回字符串的, 但是重定向的url中有userId参数. 然后我试了试ModelAndView、不用@ModelAttribute, 但是还是有参数, 所以这里暂时先直接使用重定向吧...
	@CheckToken(returnInfo = @ErrorHandler(returnValue = "redirect:/survey/mySurvey"))
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void addSurvey(@Valid Survey survey, @ModelAttribute(UserController.SESSION_USER_ID) Integer userId,
	                      HttpServletResponse response) throws IOException {
		survey.setDesigning(true);
		survey.setPageCount(0);
		survey.setUserId(userId);
		if(!surveyService.createSurvey(survey)) {
			response.sendRedirect("/error");
		}
		else {
			response.sendRedirect("/survey/designSurveyPage/" + survey.getId());
		}
	}

	@ResponseBody
	@RequestMapping(value = "deleteSurvey/{surveyId}")
	public String deleteSurvey(@PathVariable(value = "surveyId")Integer id){
		surveyService.deleteSurvey(id);
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/{surveyId}", method = RequestMethod.GET)
	public Survey getSurvey(@PathVariable("surveyId") Integer id, @ModelAttribute(UserController.SESSION_USER_ID)
	Integer userId) {
		Survey survey = surveyService.getSurveyAndPage(id);
		if(!survey.getUserId().equals(userId)) {
			return null;
		}
		return survey;
	}


}
