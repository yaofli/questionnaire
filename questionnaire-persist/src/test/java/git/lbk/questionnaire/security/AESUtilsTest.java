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

package git.lbk.questionnaire.security;

import git.lbk.questionnaire.util.AESUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AESUtilsTest {

	@Test
	public void testAES() throws Exception {
		String str = "这是明文";
		String password = "123456";
		String ciphertext = AESUtils.encrypt(str, password);
		assertNotEquals(str, ciphertext);
		assertEquals(str, AESUtils.decrypt(ciphertext, password));
	}

	@Test
	public void encrypt(){
		String str = "";
		String ciphertext = AESUtils.encrypt(str);
		System.out.println(ciphertext);
		assertEquals(str, AESUtils.decrypt(ciphertext));
	}
}