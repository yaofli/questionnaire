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

package git.lbk.questionnaire.entity;

import java.io.Serializable;

/**
 * 包含手机验证码发送次数的model, 其中identity表示发送手机验证码的标示, 可以为请求发送验证码客户的IP, 可以为用户手机号.
 */
public class SmsCount implements Serializable{

	public static final long serialVersionUID = 0L;

	private String identity;
	private Integer count;

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		SmsCount that = (SmsCount) o;

		if(count != that.count) return false;
		return !(identity != null ? !identity.equals(that.identity) : that.identity != null);

	}

	@Override
	public int hashCode() {
		int result = identity != null ? identity.hashCode() : 0;
		result = 31 * result + count;
		return result;
	}

	@Override
	public String toString() {
		return "PhoneCaptchaCount{" +
				"identity='" + identity + '\'' +
				", count=" + count +
				'}';
	}
}
