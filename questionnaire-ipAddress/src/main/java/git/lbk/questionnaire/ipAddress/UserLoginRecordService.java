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

package git.lbk.questionnaire.ipAddress;

import git.lbk.questionnaire.dao.impl.UserLoginRecordDaoImpl;
import git.lbk.questionnaire.entity.UserLoginRecord;
import org.springframework.transaction.annotation.Transactional;

/**
 * 该类为UserLoginRecordDao对应的Service类, 进一步封装了对user_login_record表的操作
 */
public class UserLoginRecordService {

	private UserLoginRecordDaoImpl loginRecordDao;

	public void setLoginRecordDao(UserLoginRecordDaoImpl loginRecordDao) {
		this.loginRecordDao = loginRecordDao;
	}

	public void init(){
		loginRecordDao.createTableWithTransaction(0);
		loginRecordDao.createTableWithTransaction(1);
		loginRecordDao.createTableWithTransaction(2);
	}

	/**
	 * 更新用户最后登录信息
	 *
	 * @param loginRecord 用户登录信息
	 */
	@Transactional
	public void updateUserLastLoginIp(UserLoginRecord loginRecord) {
		loginRecordDao.saveEntity(loginRecord);
	}

	/**
	 * 创建新的日志表
	 */
	@Transactional
	public void createNowLogTable() {
		loginRecordDao.createTable(1);
		loginRecordDao.createTable(2);
		loginRecordDao.createTable(3);
	}

}
