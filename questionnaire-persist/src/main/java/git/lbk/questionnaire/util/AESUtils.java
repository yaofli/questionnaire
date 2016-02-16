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

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * 提供AES加解密的工具类
 */
public class AESUtils {

	/**
	 * 加密
	 * @param content 明文
	 * @param password 密码
	 * @return 密文
	 */
	public static String encrypt(String content, String password) {
		BASE64Encoder base64Encoder = new BASE64Encoder();
		try {
			return base64Encoder.encode(encrypt(content.getBytes("utf-8"), password));
		}
		catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 解密
	 * @param content 密文
	 * @param password 密码
	 * @return 明文
	 */
	public static String decrypt(String content, String password) {
		BASE64Decoder base64Decoder = new BASE64Decoder();
		try {
			return new String( decrypt(base64Decoder.decodeBuffer(content), password),
					"utf-8");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 加密
	 *
	 * @param content  需要加密的内容
	 * @param password 加密密码
	 * @return 密文
	 */
	public static byte[] encrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();

 			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] result = cipher.doFinal(content);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解密
	 *
	 * @param content  待解密内容
	 * @param password 解密密钥
	 * @return 明文
	 */
	public static byte[] decrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();

			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] result = cipher.doFinal(content);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private final static String PASSWORD = "2pLxN%s*qnEprJv&";

	/**
	 * 使用固定密码加密数据(有风险!)
	 * @param context 明文
	 * @return 密文
	 */
	public static String encrypt(String context) {
		return AESUtils.encrypt(context, PASSWORD);
	}

	/**
	 * 使用固定密码解密{@link #encrypt(String)}加密的数据(不安全)
	 * @param ciphertext 密文
	 * @return 明文
	 */
	public static String decrypt(String ciphertext) {
		return AESUtils.decrypt(ciphertext, PASSWORD);
	}

}
