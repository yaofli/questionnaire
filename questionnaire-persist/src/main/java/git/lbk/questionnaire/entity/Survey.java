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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 一个完整的问卷调查
 */
public class Survey {

	/**
	 * 正常状态
	 */
	public static final Integer NORMAL_STATUS = 0;
	/**
	 * 删除状态
	 */
	public static final Integer DELETE_STATUS = 1;

	/**
	 * 无效调查. id为-1.
	 */
	public static final Survey INVALID_SURVEY;

	static {
		INVALID_SURVEY = new Survey();
		INVALID_SURVEY.setId(-1);
		INVALID_SURVEY.setUserId(-1);
		INVALID_SURVEY.setTitle("");
		INVALID_SURVEY.setDetailDescribe("");
		INVALID_SURVEY.setCreateTime(null);
		INVALID_SURVEY.setModifyTime(null);
		INVALID_SURVEY.setDesigning(true);
		INVALID_SURVEY.setPageCount(0);
		INVALID_SURVEY.setStatus(DELETE_STATUS);
		INVALID_SURVEY.setPages(new HashSet<>(0));
	}

	private Integer id;
	private Integer userId;
	@NotNull
	private String title;
	private String detailDescribe;
	private Date createTime;
	private Date modifyTime;
	private Boolean designing;
	private Integer pageCount;
	private Integer status;

	private Set<Page> pages = new HashSet<>(0);

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetailDescribe() {
		return detailDescribe;
	}

	public void setDetailDescribe(String describe) {
		this.detailDescribe = describe;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Set<Page> getPages() {
		return pages;
	}

	public void setPages(Set<Page> pages) {
		this.pages = pages;
	}

	/**
	 * 获取该对象的json字符串
	 * @return 给对象的json字符串
	 */
	public String toJson() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(this);
		}
		catch(JsonProcessingException e) {
			return "{}";
		}
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Survey survey = (Survey) o;

		if(id != null ? !id.equals(survey.id) : survey.id != null) return false;
		if(userId != null ? !userId.equals(survey.userId) : survey.userId != null) return false;
		if(title != null ? !title.equals(survey.title) : survey.title != null) return false;
		if(detailDescribe != null ? !detailDescribe.equals(survey.detailDescribe) : survey.detailDescribe != null)
			return false;
		if(createTime != null ? !createTime.equals(survey.createTime) : survey.createTime != null) return false;
		if(modifyTime != null ? !modifyTime.equals(survey.modifyTime) : survey.modifyTime != null) return false;
		if(designing != null ? !designing.equals(survey.designing) : survey.designing != null) return false;
		if(pageCount != null ? !pageCount.equals(survey.pageCount) : survey.pageCount != null) return false;
		return !(status != null ? !status.equals(survey.status) : survey.status != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (userId != null ? userId.hashCode() : 0);
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (detailDescribe != null ? detailDescribe.hashCode() : 0);
		result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
		result = 31 * result + (modifyTime != null ? modifyTime.hashCode() : 0);
		result = 31 * result + (designing != null ? designing.hashCode() : 0);
		result = 31 * result + (pageCount != null ? pageCount.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Survey{" +
				"id=" + id +
				", userId=" + userId +
				", title='" + title + '\'' +
				", detailDescribe='" + detailDescribe + '\'' +
				", createTime=" + createTime +
				", modifyTime=" + modifyTime +
				", designing=" + designing +
				", pageCount=" + pageCount +
				", status=" + status +
				'}';
	}
}
