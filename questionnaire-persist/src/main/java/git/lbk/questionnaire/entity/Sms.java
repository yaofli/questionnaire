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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;

/**
 * 包含手机验证码发送次数的model, 其中identity表示发送手机验证码的标示, 可以为客户IP 或者 手机号.
 */
public class Sms implements Serializable{

	private static final long serialVersionUID = -38794093210096359L;

	/**
	 * 注册类型
	 */
	public static final Integer REGISTER_TYPE = 0;

	private Integer id;

	@Pattern(regexp = "\\d{11}")
	private String mobile;

	private String ip;
	@NotNull
	private Integer type;

	private Date time;

	private String captcha;

	public Sms() {
		time = new Date();
	}

	public Sms(String mobile, String ip, Integer type) {
		this();
		this.mobile = mobile;
		this.ip = ip;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setIp(String identity) {
		this.ip = identity;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
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

		Sms sms = (Sms) o;

		if(ip != null ? !ip.equals(sms.ip) : sms.ip != null) return false;
		if(type != null ? !type.equals(sms.type) : sms.type != null) return false;
		return !(time != null ? !time.equals(sms.time) : sms.time != null);

	}

	@Override
	public int hashCode() {
		int result = ip != null ? ip.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (time != null ? time.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Sms{" +
				"id=" + id +
				", mobile='" + mobile + '\'' +
				", ip='" + ip + '\'' +
				", type=" + type +
				", time=" + time +
				", captcha='" + captcha + '\'' +
				'}';
	}
}
