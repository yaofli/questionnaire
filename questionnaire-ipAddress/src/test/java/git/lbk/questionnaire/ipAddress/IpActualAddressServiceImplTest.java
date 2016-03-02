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

import git.lbk.questionnaire.entity.UserLoginRecord;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * 需要注入的参数通过mock创建并注入, 所以不需要使用SpringJUnit4ClassRunner
 */
public class IpActualAddressServiceImplTest {

	private IpActualAddressService ipActualAddressService;
	private IpActualAddress ipActualAddress;
	private UserLoginRecordService addressMessageService;

	@Before
	public void setUp() throws Exception {
		ipActualAddress = EasyMock.createMock(IpActualAddress.class);
		addressMessageService = EasyMock.createMock(UserLoginRecordService.class);

		IpActualAddressServiceImpl ipActualAddressService = new IpActualAddressServiceImpl();
		ipActualAddressService.setIpActualAddress(ipActualAddress);
		ipActualAddressService.setUserLastLoginService(addressMessageService);
		ipActualAddressService.init();
		this.ipActualAddressService = ipActualAddressService;
	}

	@Test
	public void testSaveIpActualInfo() throws Exception {
		String ip = "127.0.0.1";
		String address = "河南郑州";
		EasyMock.expect(ipActualAddress.getIpActualAddress(ip))
				.andReturn(address);
		addressMessageService.updateUserLastLoginIp(EasyMock.isA(UserLoginRecord.class));

		EasyMock.replay(ipActualAddress, addressMessageService);

		ipActualAddressService.saveIpActualInfo(1, ip);
		Thread.sleep(1000);

		EasyMock.verify(ipActualAddress, addressMessageService);
	}
}