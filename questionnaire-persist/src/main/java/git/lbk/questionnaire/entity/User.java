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

import git.lbk.questionnaire.springmvc.validator.CheckEmailPhone;
import git.lbk.questionnaire.util.DateUtil;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * 包含用户信息的model.
 * 其中的lastLoginAddress和lastLoginIP分别是上次登录时的位置和IP, 可能存在一定程度上的数据冗余,
 * 原因如下:
 * 1. 从IP地址到实际的地址的转化需要通过调用远程API实现, 因此可能会有较高的延迟.
 * 而且远程接口有每秒, 每日最大请求数的限制, 所以, 将地址数据缓存下来.
 * 2. <i>我不太确定随着时间的推移, IP地址与实际地址之间的对应关系是否会发生变化,
 * 虽然个人感觉应该不会发生大范围的变化, 但是由于不敢确定, 所以缓存下来比较保险.</i>
 */
@CheckEmailPhone
public class User implements Serializable{

	public final static long serialVersionUID = 0L;

	/**
	 * 用户状态: 正常
	 */
	public final static char NORMAL_STATUS = 'n';
	/**
	 * 用户状态, 邮箱未验证
	 */
	public final static char NOT_VERIFIED = 'v';

	/**
	 * 用户类型, 普通会员
	 */
	public final static char COMMON = 'c';

	private Integer id;

	@NotNull
	@Length(min = 1, max = 50)
	private String name;
	@NotNull
	@Length(min = 6)
	private String password;

	private String autoLogin;
	private String mobile;
	private String email;

	private Character status;
	private Character type;
	private Date registerTime;
	private Date lastLoginTime;
	private String lastLoginAddress;
	private String lastLoginIp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(String autoLogin) {
		this.autoLogin = autoLogin;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String telephone) {
		this.mobile = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Character getType() {
		return type;
	}

	public void setType(Character type) {
		this.type = type;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginAddress() {
		return lastLoginAddress;
	}

	public void setLastLoginAddress(String lastLoginAddress) {
		this.lastLoginAddress = lastLoginAddress;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if(id != null ? !id.equals(user.id) : user.id != null) return false;
		if(name != null ? !name.equals(user.name) : user.name != null) return false;
		if(password != null ? !password.equals(user.password) : user.password != null) return false;
		if(autoLogin != null ? !autoLogin.equals(user.autoLogin) : user.autoLogin != null) return false;
		if(mobile != null ? !mobile.equals(user.mobile) : user.mobile != null) return false;
		if(email != null ? !email.equals(user.email) : user.email != null) return false;
		if(status != null ? !status.equals(user.status) : user.status != null) return false;
		if(type != null ? !type.equals(user.type) : user.type != null) return false;
		if(!DateUtil.equals(registerTime, user.registerTime)) return false;
		if(!DateUtil.equals(lastLoginTime, user.lastLoginTime)) return
				false;
		if(lastLoginAddress != null ? !lastLoginAddress.equals(user.lastLoginAddress) : user.lastLoginAddress != null)
			return false;
		return !(lastLoginIp != null ? !lastLoginIp.equals(user.lastLoginIp) : user.lastLoginIp != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (autoLogin != null ? autoLogin.hashCode() : 0);
		result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (registerTime != null ? registerTime.hashCode() : 0);
		result = 31 * result + (lastLoginTime != null ? lastLoginTime.hashCode() : 0);
		result = 31 * result + (lastLoginAddress != null ? lastLoginAddress.hashCode() : 0);
		result = 31 * result + (lastLoginIp != null ? lastLoginIp.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", autoLogin='" + autoLogin + '\'' +
				", telephone='" + mobile + '\'' +
				", email='" + email + '\'' +
				", status=" + status +
				", type='" + type + '\'' +
				", registerTime=" + registerTime +
				", lastLoginTime=" + lastLoginTime +
				", lastLoginAddress='" + lastLoginAddress + '\'' +
				", lastLoginIp='" + lastLoginIp + '\'' +
				'}';
	}
}
