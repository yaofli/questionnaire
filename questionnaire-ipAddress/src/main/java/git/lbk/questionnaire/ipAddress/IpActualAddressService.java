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

package git.lbk.questionnaire.ipAddress;

import git.lbk.questionnaire.model.User;

/**
 * 提供ip地址查询服务
 */
public interface IpActualAddressService {

	void init() throws Exception;

	/**
	 * 根据用户最后登录的ip获取登录的实际地址, 然后将最后登录ip, 地点, 时间保存到数据库中
	 * 该方法并不保证绝对的正确性, 比如连续调用两次该方法, 数据库中的最终数据可能是第一次时的数据, 而不是第二次的
	 *
	 * @param user user实体
	 */
	void saveIpActualInfo(User user);

	void destroy();

}
