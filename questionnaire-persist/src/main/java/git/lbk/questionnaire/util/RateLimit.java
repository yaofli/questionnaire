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

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * 实现限制速率功能.
 */
public class RateLimit {

	private JedisPool jedisPool;
	private String script;

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public void init() throws Exception {
		ClassPathResource resource = new ClassPathResource("script/ratelimiting.lua");
		script = FileCopyUtils.copyToString(new EncodedResource(resource, "UTF-8").getReader());
	}

	/**
	 * 提供限制速率的功能
	 *
	 * @param key        关键字
	 * @param expireTime 过期时间
	 * @param count      在过期时间内可以访问的次数
	 * @return 没有超过指定次数则返回true, 否则返回false
	 */
	public boolean isExceedRate(String key, long expireTime, int count) {
		List<String> params = new ArrayList<>();
		params.add(Long.toString(expireTime));
		params.add(Integer.toString(count));
		return isExceedRate(key, params);
	}

	/**
	 * 提供限制速率的功能
	 *
	 * @param key    关键字
	 * @param params 其他参数, 其中第一个值为过期时间, 第二个值为过期时间内可以访问的次数
	 * @return 超过指定次数则返回true, 否则返回false
	 */
	public boolean isExceedRate(String key, List<String> params) {
		try(Jedis jedis = jedisPool.getResource()) {
			List<String> keys = new ArrayList<>(1);
			keys.add(key);
			Long canSend = (Long) jedis.eval(script, keys, params);
			return canSend == 0;
		}
	}

}
