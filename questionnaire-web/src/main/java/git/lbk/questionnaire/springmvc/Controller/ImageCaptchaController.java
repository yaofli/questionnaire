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

import nl.captcha.Captcha;
import nl.captcha.backgrounds.BackgroundProducer;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.noise.NoiseProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.DefaultTextProducer;
import nl.captcha.text.producer.TextProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class ImageCaptchaController {

	/**
	 * 验证码在session中存储的key
	 */
	public final static String SESSION_KEY = "__" + Captcha.NAME + "__";
	/**
	 * 用户发送请求时, 验证码对应的name
	 */
	public final static String REQUEST_NAME = "__answer__";

	private final static char[] LETTER_NUMBER = ("1234567890" +
			"qwertyuiopasdfghjklzxcvbnm" +
			"QWERTYUIOPASDFGHJKLZXCVBNM").toCharArray();

	/**
	 * 图片宽度
	 */
	@Value("120")
	private int width;
	/**
	 * 图片高度
	 */
	@Value("40")
	private int height;
	/**
	 * 字符数量
	 */
	@Value("4")
	private int length;

	private TextProducer textProducer;
	private BackgroundProducer backgroundProducer;
	private NoiseProducer noiseProducer;

	@PostConstruct
	public void init(){
		textProducer = new DefaultTextProducer(length, LETTER_NUMBER);
		backgroundProducer = new GradiatedBackgroundProducer();
		noiseProducer = new CurvedLineNoiseProducer();
	}

	@RequestMapping(value = "captcha.png", method = RequestMethod.GET)
	public void getImageCaptcha(HttpSession session, HttpServletResponse response) {
		// session.removeAttribute(SESSION_KEY);
		Captcha captcha = new Captcha.Builder(width, height)
				.addText(textProducer)
				.addBackground(backgroundProducer)
				.addNoise(noiseProducer)
				.gimp()
				.build();

		session.setAttribute(SESSION_KEY, captcha.getAnswer());
		CaptchaServletUtil.writeImage(response, captcha.getImage());
	}

}
