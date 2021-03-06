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
import git.lbk.questionnaire.query.Page;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * 所有Dao的抽象父类, 提供了基本的查询, 更新操作
 *
 * @param <T> Dao需要对哪个实体类进行操作
 */
public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	@Autowired
	protected HibernateTemplate hibernateTemplate;

	private Class<T> clazz;

	public BaseDaoImpl() {
		//得到泛型话超类
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	/**
	 * 保存实体对象
	 *
	 * @param t 实体对象
	 */
	@Override
	public void saveEntity(T t) {
		hibernateTemplate.save(t);
	}

	/**
	 * 保存或者更新实体对象. 根据实体对象中的id判断, 如果已经有该id了, 则更新实体, 否则保存实体对象
	 *
	 * @param t 实体对象
	 */
	@Override
	public void saveOrUpdateEntity(T t) {
		hibernateTemplate.saveOrUpdate(t);
	}

	/**
	 * 更新实体对象
	 *
	 * @param t 实体对象
	 */
	@Override
	public void updateEntity(T t) {
		hibernateTemplate.update(t);
	}

	/**
	 * 删除实体对象
	 *
	 * @param t 实体的引用
	 */
	@Override
	public void deleteEntity(T t) {
		hibernateTemplate.delete(t);
	}

	/**
	 * 批量执行hql语句
	 *
	 * @param hql     hql语句
	 * @param objects 参数
	 * @return 受影响的行数
	 */
	protected int updateEntityByHQL(String hql, Object... objects) {
		return hibernateTemplate.bulkUpdate(hql, objects);
	}

	/**
	 * 执行原生的sql更新语句
	 *
	 * @param sql     sql语句
	 * @param objects sql参数
	 */
	protected int updateEntityBySQL(String sql, Object... objects) {
		SQLQuery query = hibernateTemplate.getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);
		for(int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		return query.executeUpdate();
	}

	/**
	 * 根据id获得实体
	 *
	 * @param id 实体id
	 * @return 匹配的实体, 如果没有找到, 则返回null.
	 */
	@Override
	public T getEntity(Serializable id) {
		return hibernateTemplate.get(clazz, id);
	}

	/**
	 * 执行hql语句, 返回所有该语句查出的记录
	 *
	 * @param hql     hql语句
	 * @param objects 参数
	 * @return 结果的List集合
	 */
	protected List<T> findEntityByHQL(String hql, Object... objects) {
		return (List<T>) hibernateTemplate.find(hql, objects);
	}

	/**
	 * 单值检索,只返回一条记录, 需要确保只有一条结果记录
	 * 由于可能只查询一个统计值, 即可能返回Integer等类型的值, 因此这里的返回类型不能为T, 只能为Object
	 *
	 * @param hql     hql语句
	 * @param objects 参数
	 * @return 查询结果
	 */
	protected Object uniqueResult(String hql, Object... objects) {
		Query query = hibernateTemplate.getSessionFactory()
				.getCurrentSession().createQuery(hql);
		for(int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		return query.uniqueResult();
	}

	/**
	 * 执行原生的sql查询(可以指定是否封装成实体)
	 *
	 * @param clazz   封装的实体的类型(如果为null, 则表示不封装)
	 * @param sql     原生sql语句
	 * @param objects sql参数
	 * @return 查询的结果类型
	 */
	protected List executeSQLQuery(Class<T> clazz, String sql, Object... objects) {
		SQLQuery query = hibernateTemplate.getSessionFactory()
				.getCurrentSession().createSQLQuery(sql);
		if(clazz != null) {
			query.addEntity(clazz);
		}
		for(int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		return query.list();
	}

	/**
	 * 执行原生的sql查询, 获得单值数据
	 *
	 * @param sql     sql语句
	 * @param objects 参数
	 * @return 查询获得的单值数据
	 */
	protected Object uniqueResultBySql(String sql, Object... objects) {
		SQLQuery query = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		for(int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		return query.uniqueResult();
	}

	/**
	 * 查询出某页的实体信息, 其中标识列为"id". 该方法相当于调用
	 * <pre>{@code
	 * findAll(page, fromAndWhereHql, "id", objects);
	 * }</pre>
	 *
	 * @param page            分页信息, 该方法会修改该参数
	 * @param fromAndWhereHql hql的from和where子句
	 * @param objects         参数
	 * @return 该页的实体信息, 结果集大小等
	 */
	protected Page<T> findAll(Page<T> page, String fromAndWhereHql, Object... objects) {
		return findAll(page, fromAndWhereHql, "id", objects);
	}

	/**
	 * 查询出某页的实体信息.
	 *
	 * @param page            分页信息, 该方法会修改该参数
	 * @param fromAndWhereHql hql的from和where子句
	 * @param flagColumn      用来统计结果集大小的标志列(比如id, *)
	 * @param objects         参数
	 * @return 该页的实体信息, 结果集大小等.
	 */
	protected Page<T> findAll(Page<T> page, String fromAndWhereHql, String flagColumn, Object... objects) {
		long totalCount = (long) uniqueResult("select count(" + flagColumn + ") " + fromAndWhereHql, objects);
		page.setTotalCount((int) totalCount);
		if(page.getFirstResult() < page.getTotalCount()) {
			page.setContent(getPageList(page, fromAndWhereHql, objects));
		}
		return page;
	}

	/**
	 * 获得指定页面的对象列表
	 *
	 * @param page    分页信息, 该方法会修改该参数
	 * @param hql     hql语句
	 * @param objects 参数
	 * @return 该页的页面信息
	 */
	private List<T> getPageList(Page<T> page, String hql, Object... objects) {
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setFirstResult(page.getFirstResult());
		query.setMaxResults(page.getPageSize());
		for(int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		return query.list();
	}

}
