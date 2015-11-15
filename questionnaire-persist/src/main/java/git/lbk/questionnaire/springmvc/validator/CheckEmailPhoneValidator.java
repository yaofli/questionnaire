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

package git.lbk.questionnaire.springmvc.validator;

import git.lbk.questionnaire.entity.User;
import git.lbk.questionnaire.util.StringUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 检验邮箱和手机号是否正确.
 * 只要邮箱和手机号有一个正确, 即视为正确
 */
public class CheckEmailPhoneValidator implements ConstraintValidator<CheckEmailPhone, User> {
	@Override
	public void initialize(CheckEmailPhone constraintAnnotation) {

	}

	@Override
	public boolean isValid(User user, ConstraintValidatorContext context) {
		if(user == null) {
			return true;
		}
		if(StringUtil.verifyMobile(user.getMobile()) || StringUtil.verifyEmail(user.getEmail())) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("手机号或者邮箱错误")
				.addConstraintViolation();
		return false;
	}
}
