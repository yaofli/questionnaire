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

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class IpActualAddressImpl implements IpActualAddress {

	/**
	 * 返回的json中国家的键
	 */
	private static final String COUNTRY = "country";

	/**
	 * 返回的json中省份的键
	 */
	private static final String PROVINCE = "province";

	/**
	 * 返回的json中城市的键
	 */
	private static final String CITY = "city";

	/**
	 * 返回的json中地区的键
	 */
	private static final String DISTRICT = "district";

	/**
	 * 返回的json中运营商的键
	 */
	private static final String CARRIER = "carrier";

	private String apiKey;
	private String httpUrl;
	private List<String> unknownData;

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public void setUnknownData(List<String> unknownData) {
		this.unknownData = unknownData;
	}

	/**
	 * 获得ip地址对应的地理信息
	 *
	 * @param ip ip地址
	 * @return ip对应的地理信息
	 * @throws CannotAcquireAddressException 无法获取ip地址信息或者获取到格式不正确的ip地址时抛出该异常
	 */
	@Override
	public String getIpActualAddress(String ip) throws CannotAcquireAddressException {
		try {
			return  getAddressInfo(getActualAddressJson(ip));
		}
		catch(Exception e) {
			CannotAcquireAddressException ex = new CannotAcquireAddressException("无法获取Ip地址的地理信息");
			ex.initCause(e);
			throw ex;
		}
	}

	/**
	 * 获取IP地址对应的真实物理地址的json数据
	 *
	 * @param ip IP地址
	 * @return 包含真实物理地址的json数据
	 * @throws IOException 如果网络连接出现故障
	 */
	private String getActualAddressJson(String ip) throws IOException {
		URL url = new URL(httpUrl + "?ip=" + ip);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("apikey", apiKey);
		connection.connect();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream(), "UTF-8"));
		StringBuilder stringBuilder = new StringBuilder();
		String strRead;

		while((strRead = reader.readLine()) != null) {
			stringBuilder.append(strRead);
			stringBuilder.append("\r\n");
		}

		reader.close();
		return stringBuilder.toString();
	}

	/**
	 * 解析返回的json字符串, 从中获取地理信息
	 *
	 * @param json json字符串
	 * @return
	 */
	private String getAddressInfo(String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		int errNum = jsonObject.getInt("errNum");
		if(errNum != 0) {
			// 这里retData是数组, 不是JSONObject
			return "";
		}

		StringBuilder stringBuilder = new StringBuilder();
		JSONObject retData = jsonObject.getJSONObject("retData");
		stringBuilder.append(deleteUnknowData(retData.getString(COUNTRY)));
		stringBuilder.append(deleteUnknowData(retData.getString(PROVINCE)));
		stringBuilder.append(deleteUnknowData(retData.getString(CITY)));
		stringBuilder.append(deleteUnknowData(retData.getString(DISTRICT)) + "\n");
		stringBuilder.append(deleteUnknowData(retData.getString(CARRIER)));
		return stringBuilder.toString();
	}

	/**
	 * 如果字符串表示未知(比如: None, 未知), 则返回空字符串. 否则返回原字符串
	 *
	 * @param str 需要处理的字符串
	 * @return 处理过的字符串
	 */
	private String deleteUnknowData(String str) {
		if(unknownData.contains(str)){
			return "";
		}
		return str;
	}

}
