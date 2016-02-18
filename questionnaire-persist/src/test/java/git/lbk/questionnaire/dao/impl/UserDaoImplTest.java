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

import git.lbk.questionnaire.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateQueryException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * 测试UserDaoImpl, 同时测试抽象父类BaseDaoImpl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:questionnaire-persistTest.xml")
public class UserDaoImplTest {

	@Autowired
	private UserDaoImpl userDao;

	private User user;

	@Before
	public void setUp() throws ParseException {
		user = new User();
		user.setId(3);
		user.setName("zs");
		user.setPassword("1234567890");
		user.setAutoLogin("zsAutoLogin");
		user.setMobile("13145679381");
		user.setEmail("zs@gmail.com");
		user.setStatus('n');
		user.setType(User.COMMON);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		user.setRegisterTime(dateFormat.parse("2014-02-06 22:08:30"));
	}

	@Test
	public void testGetEntity() throws Exception {
		assertEquals("getEntity方法获取数据失败,获取的数据为", user, userDao.getEntity(user.getId()));
		assertEquals("getEntity方法获取数据失败,获取的数据为", null, userDao.getEntity(100000));
	}

	@Test
	public void testFindEntityByHQL() throws Exception {
		List<User> list = userDao.findEntityByHQL("from User u where u.email = ?", user.getEmail());
		assertEquals("查询出的数据数量不正确", 1, list.size());
		assertEquals("查询出的数据不正确", user, list.get(0));

		list = userDao.findEntityByHQL("from User u where u.mobile = ?", "user");
		assertEquals("查询出的数据数量不正确", 0, list.size());
	}

	@Test
	public void testUniqueResult() throws Exception {
		assertEquals("查询出的数据不正确", user,
				userDao.uniqueResult("from User u where u.email = ?", user.getEmail()));

		assertEquals("查询出的数据不正确", null,
				userDao.uniqueResult("from User u where u.mobile = ?", "user"));
	}

	@Test
	public void testExecuteSQLQuery() throws Exception {
		List<Object[]> list = userDao.executeSQLQuery(null, "select * from user where id=?", user.getId());
		assertEquals("查询出的数据数量不正确", 1, list.size());
		assertEquals("查询出的数据类型不正确", new Object[0].getClass(), list.get(0).getClass());

		List<User> users = userDao.executeSQLQuery(User.class, "select * from user where id=?", user.getId());
		assertEquals("查询出的数据数量不正确", 1, users.size());
		assertEquals("查询出的数据不正确", user, users.get(0));

		users = userDao.executeSQLQuery(User.class, "select * from user where id=?", 10000000);
		assertEquals("查询出的数据数量不正确", 0, users.size());
	}

	// 下面的测试都依赖于testGetEntity

	@Test
	public void testSaveEntity() throws Exception {
		User saveUser = new User();
		saveUser.setName("saveUser");
		saveUser.setAutoLogin("saveUser");
		saveUser.setPassword("1234567'8");
		saveUser.setEmail("saveUser@gmail.com");
		saveUser.setStatus('n');
		saveUser.setRegisterTime(new Date());
		saveUser.setType(User.COMMON);

		userDao.saveEntity(saveUser);
		User u = userDao.getEntity(saveUser.getId());

		assertEquals("保存实体出错", saveUser, u);
	}

	@Test
	public void testSaveOrUpdateEntity() throws Exception {
		User saveUser = new User();
		saveUser.setName("saveOrUpdateUser");
		saveUser.setAutoLogin("saveOrUpdateUser");
		saveUser.setPassword("1234567'8");
		saveUser.setEmail("saveOrUpdateUserFirst@gmail.com");
		saveUser.setStatus('n');
		saveUser.setRegisterTime(new Date());
		saveUser.setType(User.COMMON);

		userDao.saveOrUpdateEntity(saveUser);
		assertEquals("保存实体出错", saveUser, userDao.getEntity(saveUser.getId()));

		saveUser.setEmail("saveOrUpdateUserSecond@gmail.com");
		userDao.saveOrUpdateEntity(saveUser);
		assertEquals("更新实体出错", saveUser, userDao.getEntity(saveUser.getId()));
	}

	@Test
	public void testUpdateEntity() throws Exception {
		String email = user.getEmail();
		user.setEmail("testSecond@gmail.com");
		userDao.updateEntity(user);
		assertEquals("更新实体出错", user, userDao.getEntity(user.getId()));

		// 还原数据库
		user.setEmail(email);
		userDao.updateEntity(user);
		assertEquals("更新实体出错", user, userDao.getEntity(user.getId()));
	}

	@Test
	public void testDeleteEntity() throws Exception {
		int userId = 1;
		User u = userDao.getEntity(userId);
		userDao.deleteEntity(u);
		assertEquals("删除实体失败", null, userDao.getEntity(userId));
	}

	@Test
	public void testUpdateEntityByHQL() throws Exception {
		String name = user.getName();
		user.setName("updateEntityByHQL");
		String hql = "update User u set u.name = ? where u.id = ?";

		userDao.updateEntityByHQL(hql, user.getName(), user.getId());
		assertEquals(user, userDao.getEntity(user.getId()));

		user.setName(name);
		userDao.updateEntity(user);
	}

	@Test(expected = HibernateQueryException.class)
	public void testUpdateEntityByHQLError() throws Exception {
		String hql = "update user u set u.name = ? where u.id = ?"; // User 写成了 user
		userDao.updateEntityByHQL(hql, user.getName(), user.getId());
	}

	@Test
	public void testUpdateEntityBySQL(){
		String name = user.getName();
		user.setName("updateEntityBySQL");
		String sql = "update user set name=? where id=?";
		userDao.updateEntityBySQL(sql, user.getName(), user.getId());
		assertEquals(user, userDao.getEntity(user.getId()));

		user.setName(name);
		userDao.updateEntity(user);
	}

	@Test
	public void testExecuteSQL() throws Exception {
		int userId = 2;
		User user = userDao.getEntity(userId);
		assertNotEquals("初始化数据库不正确, 没有id为" + userId + "的用户", null, user);
		userDao.updateEntityBySQL("delete from user where id = ?", userId);
		assertEquals("删除失败", null, userDao.getEntity(userId));
	}

	@Test
	public void testGetUserByAccount() throws Exception {
		assertNotNull("没有找到注册用户", userDao.getUserByAccount(user.getMobile()));
		assertNotNull("没有找到注册用户", userDao.getUserByAccount(user.getEmail()));

		assertNull("查找到为注册用户", userDao.getUserByAccount("abc"));
		assertNull("查找到用户名为空字符的用户", userDao.getUserByAccount(""));
		assertNull("查找到用户名为空的用户", userDao.getUserByAccount(null));
	}

	@Test
	public void testValidateLoginInfo() throws Exception {
		assertEquals("用户登录失败", user,
				userDao.validateLoginInfo(user.getEmail(), user.getPassword()));

		assertEquals("用户登录失败", user,
				userDao.validateLoginInfo(user.getMobile(), user.getPassword()));

		assertEquals("无效用户登录成功", null, userDao.validateLoginInfo("", "12345"));
	}

	@Test
	public void testValidateAutoLoginInfo() throws Exception {
		assertEquals("自动登录失败", user, userDao.validateAutoLoginInfo(user.getAutoLogin()));
		assertEquals("无效用户自动登录成功", null, userDao.validateAutoLoginInfo("12345"));
	}

	@Test
	public void testUpdateLastLoginInfo() {
		User u = userDao.getEntity(user.getId());
		u.setLastLoginTime(new Date());
		u.setLastLoginAddress("河南洛阳\n电信");
		u.setLastLoginIp("172.10.23.12");
		userDao.updateLastLoginInfo(u);

		assertEquals(u, userDao.getEntity(u.getId()));

		userDao.updateEntity(user);
	}

}