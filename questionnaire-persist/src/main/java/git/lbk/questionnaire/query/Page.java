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

import java.util.*;

/**
 * 分页信息
 *
 * @param <T> 保存的实体类型
 */
public class Page<T> {

	private int pageSize;
	private int pageNo;
	private int totalCount;
	private List<T> content;

	/**
	 * 使用默认的设置初始化Page对象. 默认pageSize为10, pageNo为0
	 */
	public Page() {
		this(10, 0);
	}

	/**
	 * 创建Page对象
	 *
	 * @param pageSize 页面大小
	 * @param pageNo   页码, 从0开始
	 */
	public Page(int pageSize, int pageNo) {
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		content = Collections.emptyList();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getFirstResult() {
		return pageNo * pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getTotalPage() {
		return (totalCount + pageSize - 1) / pageSize;
	}

	public int getPrevPage() {
		return pageNo <= 0 ? 0 : pageNo-1;
	}

	public int getNextPage() {
		int totalPage = getTotalPage();
		return pageNo+1 >= totalPage ? totalPage - 1 : pageNo + 1;
	}

	public List<Integer> getPageList(){
		int totalPage = getTotalPage();
		List<Integer> list = new ArrayList<>(totalPage);
		for(int i = 0; i< totalPage; i++){
			list.add(i);
		}
		return list;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Page<?> page = (Page<?>) o;

		if(pageSize != page.pageSize) return false;
		if(pageNo != page.pageNo) return false;
		if(totalCount != page.totalCount) return false;
		return !(content != null ? !content.equals(page.content) : page.content != null);

	}

	@Override
	public int hashCode() {
		int result = pageSize;
		result = 31 * result + pageNo;
		result = 31 * result + totalCount;
		result = 31 * result + (content != null ? content.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Page{" +
				"pageSize=" + pageSize +
				", pageNo=" + pageNo +
				", totalCount=" + totalCount +
				", content=" + content +
				'}';
	}
}
