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

package git.lbk.questionnaire.placeholder;

import git.lbk.questionnaire.security.AESUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.*;

/**
 * 读取加密的配置文件
 * fixme 这里所谓的加密其实就是使用固定密码把内容加密, 读取的使用使用固定密码读取. 虽然看配置文件的时候心里觉得挺"安全"的.
 * 可是密码是明文写在源码里的! 而且一直没法更换密码, 就算看不到源码, 也能暴力破解.
 * 那么这里的加密该怎么做才好呢? 如果密码不写到源码里, 就没法读取配置文件, 写到文件里又不安全. 就算使用非对称加密把密码加密了,
 * 也得在源码可以读取的地方放置公/私钥吧.
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{
	private List<String> encryptPropNames;

	public void setEncryptPropNames(List<String> encryptPropNames) {
		this.encryptPropNames = encryptPropNames;
	}

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if(encryptPropNames.contains(propertyName)){
			return AESUtils.decrypt(propertyValue);
		}
		return propertyValue;
	}

}
