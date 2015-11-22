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
 * 只要满足下面的条件就视为正确:
 *  1. 用户对象为null.
 *  或者
 *  1. 邮箱和手机号至少有一个不为null.
 *  2. 邮箱和手机号如果不为空, 则必须符合相应的规则.
 * 否则, 视为不满足条件
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
		if(user.getMobile() == null && user.getEmail() == null) {
			return false;
		}

		if(user.getMobile() != null && StringUtil.verifyMobile(user.getMobile())) {
			return true;
		}
		if(user.getEmail() != null && StringUtil.verifyEmail(user.getEmail())) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("手机号或者邮箱格式错误")
				.addConstraintViolation();
		return false;
	}
}
