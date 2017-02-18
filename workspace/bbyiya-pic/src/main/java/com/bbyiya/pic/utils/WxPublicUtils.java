package com.bbyiya.pic.utils;

import com.bbyiya.utils.RedisUtil;
/**
 * 微信公众号 全局文件
 * @author Administrator
 *
 */
public class WxPublicUtils {

	
	public static String aCCESS_TOKEN_BASE="wx_access_token_";
	//缓存有效时间
	public static int TIME_VALIDAITY=7200;
	/**
	 * 设置用户微信 access_Token 全局缓存
	 * @param userId
	 * @param accessTocken
	 */
	public static void setAccessToken(Long userId, String accessTocken) {
		String key = aCCESS_TOKEN_BASE + userId;
		RedisUtil.setString(key, accessTocken, TIME_VALIDAITY);
	}

	/**
	 * 获取微信 access_Token
	 * @param userId
	 * @return
	 */
	public static String getAccessToken(Long userId) {
		if (userId == null || userId <= 0)
			return "";
		return RedisUtil.getString(aCCESS_TOKEN_BASE + userId);
	}
	
}
