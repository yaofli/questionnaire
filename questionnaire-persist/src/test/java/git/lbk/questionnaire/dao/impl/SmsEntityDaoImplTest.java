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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:questionnaire-persistTest.xml")
public class SmsEntityDaoImplTest {

	@Autowired
	private SmsEntityDaoImpl smsCountDao;

	@Test
	public void testGetMobileCount(){
		assertEquals(3, smsCountDao.getMobileCount("12345678901"));
		assertEquals(0, smsCountDao.getMobileCount("12345678903"));
	}

	@Test
	public void testGetIPCount(){
		assertEquals(2, smsCountDao.getIPCount("127.0.0.1"));
		assertEquals(0, smsCountDao.getIPCount("127.0.0.5"));
	}

}