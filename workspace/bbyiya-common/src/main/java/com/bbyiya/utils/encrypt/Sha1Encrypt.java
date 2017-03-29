package com.bbyiya.utils.encrypt;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.bbyiya.utils.ObjectUtil;

public class Sha1Encrypt {

	
	public static String SHA1(Map<String, Object> maps) throws DigestException {
		// 获取信息摘要 - 参数字典排序后字符串
		String decrypt = getOrderByLexicographic(maps);
		try {
			// 指定sha1算法
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(decrypt.getBytes());
			// 获取字节数组
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString().toUpperCase();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new DigestException("签名错误！");
		}
	}
	
	public static String SHA1(String decrypt){
		try {
			// 指定sha1算法
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(decrypt.getBytes());
			// 获取字节数组
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString().toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static String SHA1(List<NameValuePair> maps) throws DigestException {
		// 获取信息摘要 - 参数字典排序后字符串
		String decrypt = getPackage(maps);
		try {
			// 指定sha1算法
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(decrypt.getBytes());
			// 获取字节数组
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString().toLowerCase();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new DigestException("签名错误！");
		}
	}

	private static String getPackage(List<NameValuePair> params) {

		Collections.sort(params, new Comparator<NameValuePair>() {
			// 重写排序规则
			public int compare(NameValuePair list1, NameValuePair list2) {
				return list1.getName().compareTo(list2.getName());
			}
		});
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			if (i != params.size() - 1)
				sb.append('&');
		}
		return sb.toString();
	}
	/**
	 * 获取参数的字典排序
	 * 
	 * @param maps
	 *            参数key-value map集合
	 * @return String 排序后的字符串
	 */
	private static String getOrderByLexicographic(Map<String, Object> maps) {
		return splitParams(lexicographicOrder(getParamsName(maps)), maps);
	}

	/**
	 * 获取参数名称 key
	 * 
	 * @param maps
	 *            参数key-value map集合
	 * @return
	 */
	private static List<String> getParamsName(Map<String, Object> maps) {
		List<String> paramNames = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : maps.entrySet()) {
			paramNames.add(entry.getKey());
		}
		return paramNames;
	}

	/**
	 * 参数名称按字典排序
	 * 
	 * @param paramNames
	 *            参数名称List集合
	 * @return 排序后的参数名称List集合
	 */
	private static List<String> lexicographicOrder(List<String> paramNames) {
		Collections.sort(paramNames);
		return paramNames;
	}

	/**
	 * 拼接排序好的参数名称和参数值
	 * 
	 * @param paramNames
	 *            排序后的参数名称集合
	 * @param maps
	 *            参数key-value map集合
	 * @return String 拼接后的字符串
	 */
	private static String splitParams(List<String> paramNames, Map<String, Object> maps) {
		StringBuilder paramStr = new StringBuilder();
		for (String paramName : paramNames) {
			if(!ObjectUtil.isEmpty(paramStr.toString())){
				paramStr.append("&"+paramName);
			}else {
				paramStr.append(paramName);
			}
			for (Map.Entry<String, Object> entry : maps.entrySet()) {
				if (paramName.equals(entry.getKey())) {
					paramStr.append("="+String.valueOf(entry.getValue()));
				}
			}
		}
		return paramStr.toString();
	}
}
