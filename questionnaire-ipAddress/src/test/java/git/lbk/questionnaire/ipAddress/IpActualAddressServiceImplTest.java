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

import git.lbk.questionnaire.entity.User;
import org.easymock.EasyMock;
import org.easymock.LogicalOperator;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * 需要注入的参数通过mock创建并注入, 所以不需要使用SpringJUnit4ClassRunner
 * fixme 这里同样只测试了单线程的情况, 没有测试多线程下的执行情况
 */
public class IpActualAddressServiceImplTest {

	private IpActualAddressService ipActualAddressService;
	private IpActualAddress ipActualAddress;
	private AddressMessageService addressMessageService;
	private User user;

	@Before
	public void setUp() throws Exception {
		ipActualAddress = EasyMock.createMock(IpActualAddress.class);
		addressMessageService = EasyMock.createMock(AddressMessageService.class);

		IpActualAddressServiceImpl ipActualAddressService = new IpActualAddressServiceImpl();
		ipActualAddressService.setIpActualAddress(ipActualAddress);
		ipActualAddressService.setUserLastLoginService(addressMessageService);
		ipActualAddressService.init();
		this.ipActualAddressService = ipActualAddressService;

		user = new User();
		user.setId(1);
		user.setLastLoginIp("127.0.0.1");
		user.setLastLoginAddress("河南洛阳");
		user.setLastLoginTime(new Date());
	}

	@Test
	public void testSaveIpActualInfo() throws Exception {
		EasyMock.expect(ipActualAddress.getIpActualAddress(user.getLastLoginIp()))
				.andReturn(user.getLastLoginAddress());
		addressMessageService.updateUserLastLoginIp(EasyMock.cmp(user, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				if(!u1.getId().equals(u2.getId())){
					return 1;
				}
				if(!u1.getLastLoginIp().equals(u2.getLastLoginIp())){
					return 1;
				}
				if(!u1.getLastLoginAddress().equals(u2.getLastLoginAddress())){
					return 1;
				}
				long before = u1.getLastLoginTime().getTime();
				long later = before + 3 * 60 * 1000;
				long u2Time = u2.getLastLoginTime().getTime();
				if(before>u2Time || later<u2Time){
					return 1;
				}
				return 0;
			}
		}, LogicalOperator.EQUAL));

		ipActualAddressService.saveIpActualInfo(user);

	}
}