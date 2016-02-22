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

import git.lbk.questionnaire.util.StringUtil;

import java.util.*;

/**
 * 封装查询条件的类. 包含条件和参数两部分
 */
public class QueryCondition {

	private StringBuilder condition;
	private List<Object> params;

	public QueryCondition() {
		condition = new StringBuilder();
		params = new ArrayList<>();
	}

	public QueryCondition(String condition, Object... params) {
		this.condition = new StringBuilder(condition);
		this.params = new ArrayList<>();
		this.params.addAll(Arrays.asList(params));
	}

	public String getCondition() {
		return condition.toString();
	}

	public String getConditionWithWhere() {
		return " where " + condition;
	}

	public List<Object> getParams() {
		return params;
	}

	public Object[] getParamsAsArray() {
		return params.toArray();
	}

	/**
	 * 获得代表相等条件的QueryCondition对象
	 *
	 * @param column 列名
	 * @param param  参数
	 */
	public static QueryCondition eq(String column, Object param) {
		if(param != null) {
			return new QueryCondition(column + " = ? ", param);
		}
		return new QueryCondition();
	}

	/**
	 * 获得代表不相等条件的QueryCondition对象
	 *
	 * @param column 列名
	 * @param param  参数
	 */
	public static QueryCondition notEq(String column, Object param) {
		if(param != null) {
			return new QueryCondition(column + " != ? ", param);
		}
		return new QueryCondition();
	}

	/**
	 * 获得代表like条件的QueryCondition对象
	 * @param column 列名
	 * @param param  参数
	 */
	public static QueryCondition like(String column, String param){
		if(param != null){
			return new QueryCondition(column + " like ? ", "%"+param+"%");
		}
		return new QueryCondition();
	}

	/**
	 * 使用"and"连接本对象和other对象中的条件和参数
	 *
	 * @param other 需要连接的另外一个RigUpCondition对象
	 */
	public QueryCondition and(QueryCondition other) {
		and(other.getCondition(), other.getParams());
		return this;
	}

	/**
	 * 使用"and"连接本对象的condition和参数condition, 并将参数列表相加
	 *
	 * @param condition 查询条件
	 * @param params    查询参数
	 */
	public void and(String condition, List<Object> params) {
		conjunction(condition, params, "and");
	}

	/**
	 * 使用"or"连接本对象和other对象中的条件和参数
	 *
	 * @param other 需要连接的另外一个RigUpCondition对象
	 */
	public QueryCondition or(QueryCondition other) {
		or(other.getCondition(), other.getParams());
		return this;
	}

	/**
	 * 使用"or"连接本对象的condition和参数condition, 并将参数列表相加
	 *
	 * @param condition 查询条件
	 * @param params    查询参数
	 */
	public void or(String condition, List<Object> params) {
		conjunction(condition, params, "or");
	}

	/**
	 * 使用指定的连接词连接本对象的condition和参数condition, 并将参数列表相加
	 *
	 * @param condition   查询条件
	 * @param params      查询参数
	 * @param conjunction 连接词
	 */
	private void conjunction(String condition, List<Object> params, String conjunction) {
		if(StringUtil.isNull(condition.trim())) {
			return;
		}
		if(this.condition.length() != 0) {
			this.condition.insert(0, " ( ").append(" ) ").append(conjunction);
			this.condition.append(" ( ").append(condition).append(" ) ");
		}
		else {
			this.condition.append(condition);
		}
		this.params.addAll(params);
	}

}
