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

package git.lbk.questionnaire.service;

/**
 * 验证码已失效异常
 */
public class CaptchaExpireException extends RuntimeException {
	public CaptchaExpireException() {
	}

	public CaptchaExpireException(String message) {
		super(message);
	}

	public CaptchaExpireException(String message, Throwable cause) {
		super(message, cause);
	}

	public CaptchaExpireException(Throwable cause) {
		super(cause);
	}

	public CaptchaExpireException(String message, Throwable cause, boolean enableSuppression, boolean
			writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
