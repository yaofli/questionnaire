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

import git.lbk.questionnaire.dao.impl.UserDaoImpl;
import git.lbk.questionnaire.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 提供ip地址查询服务
 */
public class IpActualAddressServiceImpl implements IpActualAddressService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private IpActualAddress ipActualAddress;
	private ExecutorService executorService;
	private UserDaoImpl userDao;

	public void setIpActualAddress(IpActualAddress ipActualAddress) {
		this.ipActualAddress = ipActualAddress;
	}

	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	@Override
	public void init() throws Exception {
		executorService = Executors.newCachedThreadPool();
	}

	/**
	 * 根据用户最后登录的ip获取登录的实际地址, 然后将最后登录ip, 地点, 时间保存到数据库中
	 * 该方法并不保证绝对的正确性, 比如连续调用两次该方法, 数据库中的最终数据可能是第一次时的数据, 而不是第二次的
	 *
	 * @param user User对象, 需要使用其中的id 和 ip
	 */
	@Override
	public void saveIpActualInfo(User user) {
		User u = new User();
		u.setId(user.getId());
		u.setLastLoginIp(user.getLastLoginIp());
		u.setLastLoginTime(new Date());
		executorService.submit(()->{
			u.setLastLoginAddress(ipActualAddress.getIpActualAddress(u.getLastLoginIp()));
			userDao.updateLastLoginInfo(u);
		});
	}

	/*
	fixme 最初上面那行 userDao.updateLastLoginInfo(u) 实际上是调用下面的 updateUserLastLogin 方法, 然后事务是加在 updateUserLastLogin 方法上的. 但是每次都提示无法获取事务. 然后就把事务加到 UserDaoImpl.updateLastLoginInfo()方法上...
	 那么, 为什么无法获取事务呢? 先声明一点, 绝对不是拼写错误, ide也帮我检查了, 旁边有事务的图标.
	下面的是我猜想, 不知道对不对:

	正常情况下的声明式事务应该是这样的:

	Controller  调用service
	代理service 开启事务, 调用相应的service方法
	service     获取事务, 处理业务

	可是, 这里的是:

	Controller    service.saveIpActualInfo()
	代理service   saveIpActualInfo方法并没有添加事务, 直接调用service对应的方法
	service      把任务添加到任务队列中, 返回

	任务队列:
	获取ip, 通过 IpActualAddressServiceImpl.this.updateUserLastLogin 将数据更新到数据库中.
	其中 IpActualAddressServiceImpl.this 引用的是 service, 而不是代理service.
	所以虽然配置了事务, 但是并没有经过 代理service. 所以, 代理service并没有机会开启事务, 然后程序就挂了..
	*/

/*
	public void updateUserLastLogin(User user){
		try {
			userDao.updateLastLoginInfo(user);
		}
		catch(Exception e) {
			logger.error("更新用户最后登录信息时发生错误", e);
		}
	}
*/

	@Override
	public void destroy() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		}
		catch(InterruptedException e) {
			logger.error("停止线程池时被中断", e);
		}
	}
}
