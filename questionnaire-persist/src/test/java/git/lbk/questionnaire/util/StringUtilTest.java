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
}