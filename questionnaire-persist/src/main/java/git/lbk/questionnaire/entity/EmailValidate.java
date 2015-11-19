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

import git.lbk.questionnaire.util.DateUtil;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;


/**
 * 用户邮件验证信息model, 包含邮箱验证码, 和验证码类型, 以及创建时间.
 */
public class EmailValidate implements Serializable{

	public static final long serialVersionUID = 0;

	public static final String REGISTER_TYPE = "reg";

	/**
	 * 邮箱验证码的过期时间, 单位为小时
	 */
	public static final int EXPIRE_TIME = 2 * 24;

	@NotNull
	@Pattern(regexp = "\\p{XDigit}{64}")
	private String identityCode;

	private User user;

	private Date createTime;

	@NotNull
	private String type;

	public String getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		EmailValidate that = (EmailValidate) o;

		if(identityCode != null ? !identityCode.equals(that.identityCode) : that.identityCode != null) return false;
		if(user != null ? !user.equals(that.user) : that.user != null) return false;
		if(!DateUtil.equals(createTime, that.createTime)) return false;
		return !(type != null ? !type.equals(that.type) : that.type != null);

	}

	@Override
	public int hashCode() {
		int result = identityCode != null ? identityCode.hashCode() : 0;
		result = 31 * result + (user != null ? user.hashCode() : 0);
		result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "EmailValidate{" +
				"identityCode='" + identityCode + '\'' +
				", user=" + user +
				", createTime=" + createTime +
				", type='" + type + '\'' +
				'}';
	}
}
