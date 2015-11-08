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

package git.lbk.questionnaire.util;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateUtilTest {

	@Test
	public void testEquals() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d1 = dateFormat.parse("1992-02-19 16:33:54");
		Timestamp d2 = new Timestamp(d1.getTime());
		Date nowDate = new Date();
		Timestamp nowTimestamp = new Timestamp(nowDate.getTime());

		assertTrue("相同日期判为不等", DateUtil.equals(d1, d2));
		assertTrue("相同日期判为不等", DateUtil.equals(nowTimestamp, nowDate));
		assertTrue("两个null值判为不等", DateUtil.equals(null, null));

		assertFalse("不同日期判为相等", DateUtil.equals(d1, nowTimestamp));
		assertFalse("不同日期判为相等", DateUtil.equals(nowDate, d2));
		assertFalse("不同日期判为相等", DateUtil.equals(null, d2));
		assertFalse("不同日期判为相等", DateUtil.equals(null, d1));
		assertFalse("不同日期判为相等", DateUtil.equals(d1, null));
	}

}