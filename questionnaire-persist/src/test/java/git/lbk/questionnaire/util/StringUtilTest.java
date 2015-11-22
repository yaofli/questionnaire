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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class StringUtilTest {

	@Test
	public void testVerifyMobile() throws Exception {
		assertTrue("验证手机号失败", StringUtil.verifyMobile("12345678901"));
		assertTrue("验证手机号失败", StringUtil.verifyMobile("123 4567 8901"));
		assertTrue("验证手机号失败", StringUtil.verifyMobile("123-4567-8901"));

		assertFalse("验证手机号失败", StringUtil.verifyMobile("1245678901"));
		assertFalse("验证手机号失败", StringUtil.verifyMobile(null));
		assertFalse("验证手机号失败", StringUtil.verifyMobile("2245678901"));
	}

	@Test
	public void testAnyNull(){
		assertTrue("null 判定为非空", StringUtil.anyNull(null));
		assertTrue("null 判定为非空", StringUtil.anyNull("123", null));
		assertTrue("null 判定为非空", StringUtil.anyNull(null, "123"));
		assertTrue("空字符串 判定为非空", StringUtil.anyNull(""));
		assertTrue("空字符串 判定为非空", StringUtil.anyNull("abc", "", "123"));

		assertFalse("非空字符串判定为空", StringUtil.anyNull("abc", "1234"));
	}

	@Test
	public void testIsNull(){
		assertTrue("空字符串判定失败: null", StringUtil.isNull(null));
		assertTrue("空字符串判定失败: \"\"", StringUtil.isNull(""));

		assertFalse("空字符串判定失败: abc", StringUtil.isNull("abc"));
	}

	@Test
	public void testHexBytesToString(){
		assertEquals("1D9F", StringUtil.hexBytesToString(new byte[]{0x1d, (byte) 0x9f}));
		assertEquals("100F", StringUtil.hexBytesToString(new byte[]{0x10, (byte) 0x0f}));
		assertEquals("", StringUtil.hexBytesToString(new byte[]{}));
		assertEquals("", StringUtil.hexBytesToString(null));
	}

}