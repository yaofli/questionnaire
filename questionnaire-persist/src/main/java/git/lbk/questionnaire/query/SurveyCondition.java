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

package git.lbk.questionnaire.query;

import git.lbk.questionnaire.entity.Survey;
import git.lbk.questionnaire.util.StringUtil;

/**
 * 查询调查问卷的条件封装
 */
public class SurveyCondition {

	private Integer userId;
	private Integer id;
	private String title;
	private Integer status;
	private Page<Survey> page;

	public SurveyCondition() {
		page = new Page<>();
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(StringUtil.isNull(title)) {
			return;
		}
		this.title = title;
		try {
			id = Integer.valueOf(title);
		}
		catch(NumberFormatException ignore) {
		}
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Page<Survey> getPage() {
		return page;
	}

	public void setPage(Page<Survey> page) {
		this.page = page;
	}

	/**
	 * 获得查询条件
	 */
	public QueryCondition getCondition(){
		QueryCondition condition = new QueryCondition();
		condition.and(QueryCondition.eq("userId", userId));
		condition.and(QueryCondition.eq("id", id).or(QueryCondition.like("title", title)));
		condition.and(QueryCondition.eq("status", status).and(QueryCondition.notEq("status", Survey.DELETE_STATUS)));
		return condition;
	}

	public String getHttpQueryString(){
		return "userId=" + (userId == null ? "" : userId)
				+ "&title=" +	(StringUtil.isNull(title) ? "" : title)
				+ "&status=" + (status == null ? "" : status)
				+ "&page.pageSize=" + page.getPageSize();
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		SurveyCondition that = (SurveyCondition) o;

		if(userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
		if(id != null ? !id.equals(that.id) : that.id != null) return false;
		if(title != null ? !title.equals(that.title) : that.title != null) return false;
		if(status != null ? !status.equals(that.status) : that.status != null) return false;
		return !(page != null ? !page.equals(that.page) : that.page != null);

	}

	@Override
	public int hashCode() {
		int result = userId != null ? userId.hashCode() : 0;
		result = 31 * result + (id != null ? id.hashCode() : 0);
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (page != null ? page.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "SurveyCondition{" +
				"userId=" + userId +
				", id=" + id +
				", title='" + title + '\'' +
				", status=" + status +
				", page=" + page +
				'}';
	}
}
