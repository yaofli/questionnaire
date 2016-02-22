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

package git.lbk.questionnaire.sms;

/**
 * 未知的验证码类型异常
 */
public class UnknownTypeException extends SendSmsFailException {
	private static final long serialVersionUID = -2628225647455363848L;

	public UnknownTypeException() {
	}

	public UnknownTypeException(String message) {
		super(message);
	}

	public UnknownTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownTypeException(Throwable cause) {
		super(cause);
	}

	public UnknownTypeException(String message, Throwable cause, boolean enableSuppression, boolean
			writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
