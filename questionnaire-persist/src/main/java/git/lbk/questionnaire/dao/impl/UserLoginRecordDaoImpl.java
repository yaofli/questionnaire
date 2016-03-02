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

package git.lbk.questionnaire.dao.impl;

import git.lbk.questionnaire.entity.UserLoginRecord;
import git.lbk.questionnaire.util.DateUtil;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component("userLoginRecord")
public class UserLoginRecordDaoImpl extends BaseDaoImpl<UserLoginRecord> {

	public void createTable(int monthExcursion) {
		String sql = "CREATE TABLE IF NOT EXISTS " + getTableName(monthExcursion) + " LIKE user_login_record";
		updateEntityBySQL(sql);
	}

	public void createTableWithTransaction(int monthExcursion) {
		String sql = "CREATE TABLE IF NOT EXISTS " + getTableName(monthExcursion) + " LIKE user_login_record";
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();
			transaction.commit();
		}
		catch(HibernateException ignore) {
			transaction.rollback();
		}
	}

	@Override
	public void saveEntity(UserLoginRecord userLoginRecord) {
		String sql = "INSERT INTO " + getNowTableName() + " (user_id, login_time, ip, address) VALUES (?, NOW(), ?, " +
				"?);";
		updateEntityBySQL(sql, userLoginRecord.getUserId(), userLoginRecord.getIp(), userLoginRecord.getAddress());
	}

	@Override
	public void saveOrUpdateEntity(UserLoginRecord userLoginRecord) {
		throw new UnsupportedOperationException("不支持该操作");
	}

	@Override
	public void updateEntity(UserLoginRecord userLoginRecord) {
		throw new UnsupportedOperationException("不支持该操作");
	}

	@Override
	public void deleteEntity(UserLoginRecord userLoginRecord) {
		throw new UnsupportedOperationException("不支持该操作");
	}

	@Override
	public UserLoginRecord getEntity(Serializable id) {
		throw new UnsupportedOperationException("不支持该操作");
	}

	/**
	 * 获得现在使用的表的名字
	 */
	private String getNowTableName() {
		return getTableName(0);
	}

	/**
	 * 获得相对现在偏移monthExcursion月的表名
	 *
	 * @param monthExcursion 偏移的月数
	 * @return 对应月的表名
	 */
	private String getTableName(int monthExcursion) {
		Date date = DateUtil.getExcursionDate(new Date(), Calendar.MONTH, monthExcursion);
		return "user_login_record_" + DateUtil.format(date, "yyyy_MM");
	}

}
