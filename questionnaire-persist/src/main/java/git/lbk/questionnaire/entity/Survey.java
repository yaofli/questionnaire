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

import java.util.*;

/**
 * 一个完整的问卷调查
 */
public class Survey {
	private Integer id;
	private User user;
	private String title;
	private Date createTime;
	private Date modifyTime;
	private Boolean designing;
	private Integer pageCount;

	private Set<Page> pages;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Boolean getDesigning() {
		return designing;
	}

	public void setDesigning(Boolean designing) {
		this.designing = designing;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Set<Page> getPages() {
		return pages;
	}

	public void setPages(Set<Page> pages) {
		this.pages = pages;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Survey survey = (Survey) o;

		if(id != null ? !id.equals(survey.id) : survey.id != null) return false;
		if(title != null ? !title.equals(survey.title) : survey.title != null) return false;
		if(designing != null ? !designing.equals(survey.designing) : survey.designing != null) return false;
		return !(pageCount != null ? !pageCount.equals(survey.pageCount) : survey.pageCount != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (user != null ? user.hashCode() : 0);
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
		result = 31 * result + (modifyTime != null ? modifyTime.hashCode() : 0);
		result = 31 * result + (designing != null ? designing.hashCode() : 0);
		result = 31 * result + (pageCount != null ? pageCount.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Survey{" +
				"id=" + id +
				", user=" + user +
				", title='" + title + '\'' +
				", createTime=" + createTime +
				", modifyTime=" + modifyTime +
				", designing=" + designing +
				", pageCount=" + pageCount +
				'}';
	}
}
