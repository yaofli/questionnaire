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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import git.lbk.questionnaire.util.ORMUtil;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * 一个完整的问卷调查
 */
public class Survey implements Serializable{

	private static final long serialVersionUID = 5173875894602974070L;

	/**
	 * 正常状态
	 */
	public static final Integer NORMAL_STATUS = 0;

	/**
	 * 设计状态
	 */
	public static final Integer DESIGN_STATUS = 1;

	/**
	 * 删除状态
	 */
	public static final Integer DELETE_STATUS = 2;

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
		INVALID_SURVEY.setStatus(DELETE_STATUS);
		INVALID_SURVEY.setPages(Collections.emptyList());
	}

	private Integer id;
	private Integer userId;
	@NotNull
	private String title;
	private String detailDescribe;
	private Date createTime;
	private Date modifyTime;
	private Integer status;

	private List<Page> pages = new ArrayList<>(0);

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

	public Integer getPageCount() {
		return pages.size();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
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

	@JsonIgnore
	public boolean isNormal(){
		return NORMAL_STATUS.equals(status);
	}

	@JsonIgnore
	public boolean isDesign(){
		return DESIGN_STATUS.equals(status);
	}

	@JsonIgnore
	public boolean isDelete(){
		return DELETE_STATUS.equals(status);
	}

	/**
	 * 翻转设计状态. 如果是正常状态, 则置为设计状态; 如果是设计状态, 则置为正常状态; 否则没有任何动作
	 * @return 如果是正常状态或者设计状态, 则返回true; 否则返回false.
	 */
	public boolean reverseDesign(){
		if(isNormal()){
			status = DESIGN_STATUS;
		}
		else if(isDesign()){
			status = NORMAL_STATUS;
		}
		else{
			return false;
		}
		return true;
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
				", status=" + status +
				", pages=" + ORMUtil.toString(pages) +
				'}';
	}
}
