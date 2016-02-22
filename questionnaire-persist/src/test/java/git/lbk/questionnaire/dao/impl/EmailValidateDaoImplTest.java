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

import git.lbk.questionnaire.entity.EmailValidate;
import git.lbk.questionnaire.entity.User;
import git.lbk.questionnaire.util.DateUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:questionnaire-persistTest.xml")
public class EmailValidateDaoImplTest {

	@Autowired
	private EmailValidateDaoImpl emailValidateDao;

	@Test
	public void testDeleteByUserIDAndType() throws Exception {
		User user = new User();
		user.setId(1);
		emailValidateDao.deleteByUserIDAndType(user, "reg");
		assertNull(emailValidateDao.getEntity("1"));
		assertNull(emailValidateDao.getEntity("3"));

		assertNotNull(emailValidateDao.getEntity("2"));
		assertNotNull(emailValidateDao.getEntity("4"));
	}

	@Ignore("需要修改test-data.sql中的数据")
	@Test
	public void testDeleteBeforeTime(){
		assertNotNull(emailValidateDao.getEntity("11"));
		assertNotNull(emailValidateDao.getEntity("12"));
		assertNotNull(emailValidateDao.getEntity("13"));
		assertNotNull(emailValidateDao.getEntity("14"));
		assertNotNull(emailValidateDao.getEntity("15"));
		assertNotNull(emailValidateDao.getEntity("16"));

		emailValidateDao.deleteBeforeTime(DateUtil.getExcursionDateByNow(Calendar.HOUR_OF_DAY, -EmailValidate.EXPIRE_TIME));

		assertNull(emailValidateDao.getEntity("11"));
		assertNull(emailValidateDao.getEntity("13"));

		assertNotNull(emailValidateDao.getEntity("12"));
		assertNotNull(emailValidateDao.getEntity("14"));
		assertNotNull(emailValidateDao.getEntity("15"));
		assertNotNull(emailValidateDao.getEntity("16"));
	}
}