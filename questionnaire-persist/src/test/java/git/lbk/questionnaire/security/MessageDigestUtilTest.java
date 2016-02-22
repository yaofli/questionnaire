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

import git.lbk.questionnaire.util.MessageDigestUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageDigestUtilTest {

	@Test
	public void testSHA256() throws Exception {
		assertEquals("BA7816BF8F01CFEA414140DE5DAE2223B00361A396177A9CB410FF61F20015AD", MessageDigestUtil.SHA256("abc"));
		assertEquals("E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855", MessageDigestUtil.SHA256(""));

	}

	@Test
	public void testSHA512() throws Exception {
		assertEquals("DDAF35A193617ABACC417349AE20413112E6FA4E89A97EA20A9EEEE64B55D" +
				"39A2192992A274FC1A836BA3C23A3FEEBBD454D4423643CE80E2A9AC94FA54CA49F",
				MessageDigestUtil.SHA512("abc"));
		assertEquals("CF83E1357EEFB8BDF1542850D66D8007D620E4050B5715DC83F4A921D36CE" +
				"9CE47D0D13C5D85F2B0FF8318D2877EEC2F63B931BD47417A81A538327AF927DA3E",
				MessageDigestUtil.SHA512(""));
	}
}