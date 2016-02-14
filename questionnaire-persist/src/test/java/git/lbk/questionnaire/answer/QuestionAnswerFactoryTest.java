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

package git.lbk.questionnaire.answer;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class QuestionAnswerFactoryTest {

	@Test
	public void testSplitAnswer() throws Exception {
		String answer = "\u001E1\u001F2\u001E2\u001F3,4,5\u001E3\u001F4";
		Map<Integer, String> map = QuestionAnswerFactory.splitAnswer(answer);
		assertEquals(map.size(), 3);
		assertEquals(map.get(1), "2");
		assertEquals(map.get(2), "3,4,5");
		assertEquals(map.get(3), "4");
	}

	@Test
	public void testSplitAnswerNull() throws Exception {
		String answer = "\u001E1\u001F";
		Map<Integer, String> map = QuestionAnswerFactory.splitAnswer(answer);
		assertEquals(map.size(), 1);
		assertEquals(map.get(1), null);
	}

	@Test(expected = NumberFormatException.class)
	public void testSplitAnswerException() throws Exception {
		String answer = "\u001E1\u001F2\u001E2a\u001F3,4,5\u001E3\u001F4";
		QuestionAnswerFactory.splitAnswer(answer);
	}

}