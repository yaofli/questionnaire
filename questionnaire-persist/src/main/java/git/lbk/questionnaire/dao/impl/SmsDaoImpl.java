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

import git.lbk.questionnaire.entity.Sms;
import git.lbk.questionnaire.util.DateUtil;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;

@Repository("smsDao")
public class SmsDaoImpl extends BaseDaoImpl<Sms> {

	/**
	 * 创建新的日志表
	 *
	 * @param monthExcursion 偏移指定的月份
	 */
	public void createTable(int monthExcursion) {
		String sql = "CREATE TABLE IF NOT EXISTS " + getTableName(monthExcursion) + " LIKE sms";
		updateEntityBySQL(sql);
	}

	/**
	 * 创建新的日志表. 该方法会自动开启, 提交/回滚事务, 因此. 无需再service层开启事务.
	 * @param monthExcursion 偏移指定的月份
	 */
	public void createTableWithTransaction(int monthExcursion){
		String sql = "CREATE TABLE IF NOT EXISTS " + getTableName(monthExcursion) + " LIKE sms";
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try{
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();
			transaction.commit();
		}
		catch(HibernateException ignore){
			transaction.rollback();
		}
	}

	/**
	 * 保存SmsEntity实体对象
	 */
	@Override
	public void saveEntity(Sms sms) {
		String sql = "INSERT INTO " + getNowTableName() + "(mobile, ip, type) VALUES(?, ?, ?)";
		updateEntityBySQL(sql, sms.getMobile(), sms.getIp(), sms.getType());
	}

	/**
	 * 获得指定手机号今天请求发送短信的次数
	 *
	 * @param mobile 用户手机号
	 * @return 今天请求发送短信的次数
	 */
	public long getMobileCount(String mobile) {
		String sql = "SELECT count(id) FROM " + getNowTableName() + " WHERE mobile=? AND time >= CURDATE()";
		return ((BigInteger) uniqueResultBySql(sql, mobile)).longValue();
	}

	/**
	 * 获得指定ip今天请求发送短信的次数
	 *
	 * @param ip 用户ip
	 * @return 今天请求发送短信的次数
	 */
	public long getIPCount(String ip) {
		String sql = "SELECT count(id) FROM " + getNowTableName() + " WHERE ip=? AND time >= CURDATE()";
		return ((BigInteger) uniqueResultBySql(sql, ip)).longValue();
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
		return "sms_" + DateUtil.format(date, "yyyy_MM");
	}

}
