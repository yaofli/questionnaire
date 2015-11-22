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

package git.lbk.questionnaire.security;

import git.lbk.questionnaire.util.StringUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 对java.security.MessageDigest类的进一步封装,用户只需调用相应的方法，即可的到相应的hash值。
 * 另外提供了一个工具方法,用于将生成的hash值转换为0~9和A-F的字符串表示
 * 
 * @author lbk
 *
 */
public class MessageDigestUtil {


	/**
	 * 使用SHA-256算法将一个字符串转换为一个哈希值的字符串形式
	 * 
	 * @param str 源字符串
	 * @return SHA-256哈希值的字符串形式
	 */
	public static String SHA256( String str ){
		return StringUtil.hexBytesToString(getHashBytes(str, "SHA-256"));
	}

	/**
	 * 使用SHA-256算法将一个字符串转换为一个哈希值的byte数组形式
	 * 
	 * @param str 源字符串
	 * @return SHA-256哈希值的byte数组形式
	 */
	public static byte[] SHA256Byte( String str ){
		return getHashBytes( str, "SHA-256" );
	}

	/**
	 * 使用SHA-512算法将一个字符串转换为一个哈希值字符串形式
	 * 
	 * @param str 源字符串
	 * @return 哈希值的字符串形式
	 */
	public static String SHA512( String str ){
		return StringUtil.hexBytesToString( getHashBytes( str, "SHA-512" ) );
	}

	/**
	 * 使用SHA-256算法将一个字符串转换为一个哈希值的byte数组形式
	 * 
	 * @param str 源字符串
	 * @return SHA-256哈希值的byte数组形式
	 */
	public static byte[] SHA512Byte( String str ){
		return getHashBytes( str, "SHA-512" );
	}
	
	/**
	 * 使用指定的信息摘要算法将字符串转换为一个哈希值
	 * 
	 * @param origin 源字符串
	 * @param algorithm 信息摘要算法的名称
	 * @return 哈希值
	 */
	private static byte[] getHashBytes( String origin, String algorithm ){
		byte[] md = null;
		
		try{
			MessageDigest mdInst = MessageDigest.getInstance( algorithm );
			mdInst.update( origin.getBytes() );
			md = mdInst.digest();
		}
		catch( NoSuchAlgorithmException e ){
			e.printStackTrace();
		}
		
		return md;
	}


}
