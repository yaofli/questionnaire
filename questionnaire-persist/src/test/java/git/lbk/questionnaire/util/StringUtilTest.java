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

import static git.lbk.questionnaire.util.StringUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;


public class StringUtilTest {

	@Test
	public void testVerifyMobile() throws Exception {
		assertTrue("验证手机号失败: 12345678901", verifyMobile("12345678901"));

		assertFalse("验证手机号失败: 1245678901", verifyMobile("1245678901"));
		assertFalse("验证手机号失败: null", verifyMobile(null));
		assertFalse("验证手机号失败: 2245678901", verifyMobile("2245678901"));
	}

	@Test
	public void testVerifyEmail() throws Exception {
		assertTrue("验证邮箱失败: i.am.t.lbk@gmail.com", verifyEmail("i.am.t.lbk@gmail.com"));
		assertTrue("验证邮箱失败: 12345@qq.com", verifyEmail("12345@qq.com"));
		assertTrue("验证邮箱失败: ksj@163.com", verifyEmail("ksj@163.com"));

		assertFalse("验证邮箱失败: @163.com", verifyEmail("@163.com"));
		assertFalse("验证邮箱失败: ksj@", verifyEmail("ksj@"));
	}

	@Test
	public void testAnyNull(){
		assertTrue("null 判定为非空", anyNull((String)null));
		assertTrue("null 判定为非空", anyNull("123", null));
		assertTrue("null 判定为非空", anyNull(null, "123"));
		assertTrue("空字符串 判定为非空", anyNull(""));
		assertTrue("空字符串 判定为非空", anyNull("abc", "", "123"));

		assertFalse("非空字符串判定为空", anyNull("abc", "1234"));
	}

	@Test
	public void testIsNull(){
		assertTrue("空字符串判定失败: null", isNull(null));
		assertTrue("空字符串判定失败: \"\"", isNull(""));

		assertFalse("空字符串判定失败: abc", isNull("abc"));
	}

	@Test
	public void testHexBytesToString(){
		assertEquals("1D9F", hexBytesToString(new byte[]{0x1d, (byte) 0x9f}));
		assertEquals("100F", hexBytesToString(new byte[]{0x10, (byte) 0x0f}));
		assertEquals("", hexBytesToString(new byte[]{}));
		assertEquals("", hexBytesToString(null));
	}

	@Test
	public void testSubStringCount(){
		String originStr = "12abc12jid.,122., 1212";
		String subStr = "12";
		assertEquals(5, subStringCount(originStr, subStr));
		assertEquals(0, subStringCount("", subStr));
		assertEquals(0, subStringCount("adasdfuejaksjd90883734lajd", subStr));
	}

}