package com.bbyiya.pic.utils;

import com.bbyiya.utils.RedisUtil;
/**
 * ΢�Ź��ں� ȫ���ļ�
 * @author Administrator
 *
 */
public class WxPublicUtils {

	
	public static String aCCESS_TOKEN_BASE="wx_access_token_";
	//������Чʱ��
	public static int TIME_VALIDAITY=7200;
	/**
	 * �����û�΢�� access_Token ȫ�ֻ���
	 * @param userId
	 * @param accessTocken
	 */
	public static void setAccessToken(Long userId, String accessTocken) {
		String key = aCCESS_TOKEN_BASE + userId;
		RedisUtil.setString(key, accessTocken, TIME_VALIDAITY);
	}

	/**
	 * ��ȡ΢�� access_Token
	 * @param userId
	 * @return
	 */
	public static String getAccessToken(Long userId) {
		if (userId == null || userId <= 0)
			return "";
		return RedisUtil.getString(aCCESS_TOKEN_BASE + userId);
	}
	
}
