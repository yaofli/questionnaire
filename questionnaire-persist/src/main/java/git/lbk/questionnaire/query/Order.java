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

/**
 * 排序条件. 可以指定需要排序的列和升序或降序
 */
public class Order {

	private boolean ascending;
	private String column;

	/**
	 * 实例化一个Order, 按column升序排列
	 */
	public static Order asc(String column){
		return new Order(true, column);
	}

	/**
	 * 实例化一个Order, 按column降序排列
	 */
	public static Order desc(String column){
		return new Order(false, column);
	}

	/**
	 * 实例化Order实例
	 * @param ascending 是否升序
	 * @param column 列名
	 */
	public Order(boolean ascending, String column) {
		this.ascending = ascending;
		this.column = column;
	}

	public boolean isAscending() {
		return ascending;
	}

	public String getColumn() {
		return column;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Order order = (Order) o;

		if(ascending != order.ascending) return false;
		return !(column != null ? !column.equals(order.column) : order.column != null);
	}

	@Override
	public int hashCode() {
		int result = (ascending ? 1 : 0);
		result = 31 * result + (column != null ? column.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Order{" +
				"ascending=" + ascending +
				", column='" + column + '\'' +
				'}';
	}
}
