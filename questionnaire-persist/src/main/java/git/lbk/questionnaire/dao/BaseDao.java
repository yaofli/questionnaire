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

package git.lbk.questionnaire.dao;

import java.io.Serializable;

public interface BaseDao<T> {
	//写操作

	/**
	 * 保存实体数据
	 * @param t 实体对象
	 */
	void saveEntity(T t);

	/**
	 * 保存或者更新实体. 首先根据t中的id判断是否数据库中是否存在该实体, 如果不存在, 则保存该实体, 否则更新该实体
	 * @param t 实体对象
	 */
	void saveOrUpdateEntity(T t);

	/**
	 * 更新实体.
	 * @param t 实体对象
	 */
	void updateEntity(T t);

	/**
	 * 删除实体
	 * @param t 实体的引用, 只使用实体中的id属性
	 */
	void deleteEntity(T t);

	//读操作

	/**
	 * 根据id加载实体
	 * @param id 实体id
	 * @return 匹配的实体代理, 可能存在延迟加载.
	 *          如果使用了延迟加载, 在使用时发现没有匹配的实体, 则抛出异常.
	 *          在使用之前如果session已经关闭, 则会抛出LazyLoadException异常
	 */
	T loadEntity(Serializable id);

	/**
	 * 根据id获得实体
	 * @param id 实体id
	 * @return 匹配的实体, 如果没有找到, 则返回null.
	 */
	T getEntity(Serializable id);

}
