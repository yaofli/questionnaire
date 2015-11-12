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

/**
 * 获得IP地址对应的实际位置
 */
public interface IpActualAddress {

	/**
	 * 获得ip地址对应的地理信息
	 * @param ip ip地址
	 * @return ip对应的物理地址
	 * @throws CannotAcquireAddressException 无法获取ip地理信息或者获取到格式不正确的ip地址时抛出该异常
	 */
	String getIpActualAddress(String ip) throws CannotAcquireAddressException;

}
