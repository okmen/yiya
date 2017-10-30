package com.bbyiya.utils;

import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.bbyiya.utils.pay.WxPayConfig;
import com.bbyiya.utils.pay.WxPayUtils;

public class AccessTokenUtils {
	// 微信access_token
	public static String ACCESS_TOKEN = "wx_access_token";
	// 微信access_token 缓存有效时间
	public static int ACCESS_TOKEN_TIMEVAL = 7000;

	/**
	 * 获取微信 access_Token
	 * @param userId
	 * @return
	 */
	public static String getAccessToken() {
		String tokens = RedisUtil.getString(ACCESS_TOKEN);
		if (ObjectUtil.isEmpty(tokens)) {
			tokens = getAccessTokenPost();
			if (!ObjectUtil.isEmpty(tokens)) {
				RedisUtil.setString(ACCESS_TOKEN, tokens, ACCESS_TOKEN_TIMEVAL);
				try {
					// redis同步参数
					String postParam = "key=" + ACCESS_TOKEN + "&value=" + tokens + "&seconds=" + ACCESS_TOKEN_TIMEVAL;
					List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
					packageParams.add(new BasicNameValuePair("key", ACCESS_TOKEN));
					packageParams.add(new BasicNameValuePair("value", tokens));// 商户号
					packageParams.add(new BasicNameValuePair("seconds", String.valueOf(ACCESS_TOKEN_TIMEVAL)));
					// 生成签名
					String sign = WxPayUtils.genPackageSign(packageParams);
					postParam += "&sign=" + sign;
					String currentDomain = ConfigUtil.getSingleValue("currentDomain");
					if (!ObjectUtil.isEmpty(currentDomain) && currentDomain.contains("photo-net.")) {
						HttpRequestHelper.sendPost("https://mpic.bbyiya.com/wx/SynRedist", postParam);
					} else {
						HttpRequestHelper.sendPost("https://mpic.bbyiya.net/wx/SynRedist", postParam);
					}
				} catch (Exception e) {
				}
			}
			return tokens;
		} else {
			return tokens;
		}
	}

	
	/**
	 * 
	 * @return
	 */
	private static String getAccessTokenPost() {
		String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WxPayConfig.APPID + "&secret=" + WxPayConfig.AppSecret;
		String postResult = HttpRequestHelper.sendPost(tokenUrl, "");
		JSONObject modelToken = JSONObject.fromObject(postResult);
		if (modelToken != null && modelToken.get("access_token") != null) {
			String accessToken = modelToken.getString("access_token");
			return accessToken;
		}
		return "";
	}

}
