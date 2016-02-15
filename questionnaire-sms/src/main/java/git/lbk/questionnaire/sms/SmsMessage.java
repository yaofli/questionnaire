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

package git.lbk.questionnaire.sms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 短信内容
 */
public class SmsMessage {

	public final static String REGISTER = "reg";

	@Pattern(regexp = "\\d{11}")
	private String mobile;

	private String ip;

	@NotNull
	private String type;

	private String captcha;

	public SmsMessage() {
	}

	public SmsMessage(String mobile, String ip, String userName, String type, String captcha) {
		this.mobile = mobile;
		this.ip = ip;
		this.type = type;
		this.captcha = captcha;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		SmsMessage that = (SmsMessage) o;

		if(mobile != null ? !mobile.equals(that.mobile) : that.mobile != null) return false;
		if(ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
		if(type != null ? !type.equals(that.type) : that.type != null) return false;
		return !(captcha != null ? !captcha.equals(that.captcha) : that.captcha != null);

	}

	@Override
	public int hashCode() {
		int result = mobile != null ? mobile.hashCode() : 0;
		result = 31 * result + (ip != null ? ip.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (captcha != null ? captcha.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "SmsMessage{" +
				"mobile='" + mobile + '\'' +
				", ip='" + ip + '\'' +
				", type='" + type + '\'' +
				", captcha='" + captcha + '\'' +
				'}';
	}
}
