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

package git.lbk.questionnaire.dao.impl;

import git.lbk.questionnaire.dao.BaseDao;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	@Autowired
	private SessionFactory sf;

	private Class<T> clazz;

	public BaseDaoImpl() {
		//得到泛型话超类
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}


	/**
	 * 保存实体对象
	 * @param t 实体对象
	 */
	@Override
	public void saveEntity(T t) {
		sf.getCurrentSession().save(t);
	}

	/**
	 * 保存或者更新实体对象. 根据实体对象中的id判断, 如果已经有该id了, 则更新实体, 否则保存实体对象
	 * @param t 实体对象
	 */
	@Override
	public void saveOrUpdateEntity(T t) {
		sf.getCurrentSession().saveOrUpdate(t);
	}

	/**
	 * 更新实体对象
	 * @param t 实体对象
	 */
	@Override
	public void updateEntity(T t) {
		sf.getCurrentSession().update(t);
	}

	/**
	 * 删除实体对象
	 * @param t 实体的引用
	 */
	@Override
	public void deleteEntity(T t) {
		sf.getCurrentSession().delete(t);
	}

	/**
	 * 批量执行hql语句
	 * @param hql hql语句
	 * @param objects 参数
	 * @return 受影响的行数
	 */
	public int batchEntityByHQL(String hql, Object... objects) {
		Query query = sf.getCurrentSession().createQuery(hql);
		for(int i=0; i<objects.length; i++){
			query.setParameter(i, objects[i]);
		}
		return query.executeUpdate();
	}

	/**
	 * 执行原生的sql更新语句
	 * @param sql     sql语句
	 * @param objects sql参数
	 */
	public int executeSQL(String sql, Object... objects){
		SQLQuery query = sf.getCurrentSession().createSQLQuery(sql);
		for(int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		return query.executeUpdate();
	}

	/**
	 * 根据id加载实体
	 *
	 * @param id 实体id
	 * @return 匹配的实体代理, 可能存在延迟加载.
	 * 如果使用了延迟加载, 在使用时发现没有匹配的实体, 则抛出异常.
	 * 在使用之前如果session已经关闭, 则会抛出LazyLoadException异常
	 */
	@Override
	public T loadEntity(Serializable id) {
		return (T) sf.getCurrentSession().load(clazz, id);
	}

	/**
	 * 根据id获得实体
	 *
	 * @param id 实体id
	 * @return 匹配的实体, 如果没有找到, 则返回null.
	 */
	@Override
	public T getEntity(Serializable id) {
		return (T) sf.getCurrentSession().get(clazz, id);
	}

	/**
	 * 执行hql语句, 返回所有该语句查出的记录
	 *
	 * @param hql     hql语句
	 * @param objects 参数
	 * @return 结果的List集合
	 */
	public List<T> findEntityByHQL(String hql, Object... objects) {
		Query query = sf.getCurrentSession().createQuery(hql);
		for(int i=0; i<objects.length; i++){
			query.setParameter(i, objects[i]);
		}
		return query.list();
	}

	/**
	 * 单值检索,只返回一条记录, 需要确保只有一条结果记录
	 * 由于可能只查询一个统计值, 即可能返回Integer等类型的值, 因此这里的返回类型不能为T, 只能为Object
	 *
	 * @param hql     hql语句
	 * @param objects 参数
	 * @return 查询结果
	 */
	public Object uniqueResult(String hql, Object... objects) {
		Query query = sf.getCurrentSession().createQuery(hql);
		for(int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		return query.uniqueResult();
	}

	/**
	 * 执行原生的sql查询(可以指定是否封装成实体)
	 *
	 * @param clazz 封装的实体的类型
	 * @param sql     原生sql语句
	 * @param objects sql参数
	 * @return 查询的结果类型
	 */
	public List executeSQLQuery(Class<T> clazz, String sql, Object... objects) {
		SQLQuery query = sf.getCurrentSession().createSQLQuery(sql);
		if(clazz != null) {
			query.addEntity(clazz);
		}
		for(int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		return query.list();
	}
}
