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

package git.lbk.questionnaire.entity;

import java.io.Serializable;
import java.util.*;

public class UserLoginRecord implements Serializable {
	private static final long serialVersionUID = -2369258876479472280L;

	private Integer id;
	private Integer userId;
	private Date time;
	private String ip;
	private String address;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		UserLoginRecord that = (UserLoginRecord) o;

		if(id != null ? !id.equals(that.id) : that.id != null) return false;
		if(userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
		if(time != null ? !time.equals(that.time) : that.time != null) return false;
		if(ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
		return !(address != null ? !address.equals(that.address) : that.address != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (userId != null ? userId.hashCode() : 0);
		result = 31 * result + (time != null ? time.hashCode() : 0);
		result = 31 * result + (ip != null ? ip.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UserLoginRecord{" +
				"id=" + id +
				", userId=" + userId +
				", time=" + time +
				", ip='" + ip + '\'' +
				", address='" + address + '\'' +
				'}';
	}
}
