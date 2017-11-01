package com.bbyiya.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.bbyiya.utils.ObjectUtil;

public class UrlEncodeUtils {
	/**
	 * 默认编码方式
	 */
	private static String defaultEnc = "utf-8";

	/**
	 * url编码
	 * 
	 * @param paramStr
	 * @param enc
	 * @return
	 */
	public static String urlEncode(String paramStr, String enc) {
		try {
			return URLEncoder.encode(paramStr, ObjectUtil.isEmpty(enc) ? defaultEnc : enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return paramStr;
	}

	/**
	 * URL解码
	 * 
	 * @param paramStr
	 * @param enc
	 *            解码方式 默认 utf-8
	 * @return
	 */
	public static String urlDecode(String paramStr, String enc) {
		try {
			return URLDecoder.decode(paramStr, ObjectUtil.isEmpty(enc) ? defaultEnc : enc);
		} catch (UnsupportedEncodingException e) {
			return paramStr;
		}
	}

}
