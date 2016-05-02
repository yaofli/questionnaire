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

package git.lbk.questionnaire.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:questionnaire-persistTest.xml")
public class RateLimitTest {

	@Autowired
	private RateLimit rateLimit;

	@Test
	public void testIsExceedRate() throws Exception {
		Assert.isTrue(!rateLimit.isExceedRate("rateLimit1", 2, 1), "限制频率调用失败. 应该可以通过, 结果却没有");
		Assert.isTrue(rateLimit.isExceedRate("rateLimit1", 2, 1), "限制频率调用失败. 应该不能通过, 结果却通过了");
	}

}