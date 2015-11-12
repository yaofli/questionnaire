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

package git.lbk.questionnaire.email;

import git.lbk.questionnaire.util.DateUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.util.*;

public class SendMailServiceImpl implements SendMailService {

	private AsyncSendMail asyncSendMail;
	private String templatePath;

	private String registerTemplate;

	public void setAsyncSendMail(AsyncSendMail asyncSendMail) {
		this.asyncSendMail = asyncSendMail;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	@Override
	public void init() throws Exception{
		registerTemplate = loadFile("registerUser.html");
	}

	private String loadFile(String fileName){
		ClassPathResource resource = new ClassPathResource(templatePath + File.separator + fileName);
		try {
			return  FileCopyUtils.copyToString(new EncodedResource(resource, "UTF-8").getReader());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private String getMailText(String template, Map<String, String> args) {
		for(Map.Entry<String, String> arg : args.entrySet()){
			template = template.replace("${"+arg.getKey()+"}", arg.getValue());
		}
		return template;
	}

	@Override
	public void sendRegisterMail(String username, String captcha, String userEmail) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, 2 * 24);
		Map<String, String> map = new HashMap<>();
		map.put("username", username);
		map.put("captcha", captcha);
		map.put("expireTime", DateUtil.format(calendar.getTime(), "yyyy-MM-dd hh:mm:ss"));
		map.put("currentTime", DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));

		MailMessage mailMessage = new MailMessage();
		mailMessage.setMessage(getMailText(registerTemplate, map));
		mailMessage.setTo(userEmail);
		mailMessage.setSubject("XX账号-账号激活");
		asyncSendMail.asynchronismSendMail(mailMessage);
	}

	@Override
	public void destroy() {

	}
}
