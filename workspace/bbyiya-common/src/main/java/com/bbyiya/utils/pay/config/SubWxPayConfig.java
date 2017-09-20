package com.bbyiya.utils.pay.config;

import com.bbyiya.utils.ConfigUtil;

public class SubWxPayConfig {
	/**
	 * 商户号(幻想馆) ps:微信支付分配的商户号
	 */
	public static String MCH_ID = "1488433062"; 
	/**
	 * 微信支付分配的子商户号
	 */
	public	static String SUB_MCH_ID="1488843162";
	/**
	 * 微信分配的公众账号ID
	 */
	public static String APPID = "wxcc101e7b17ed868e";
	/**
	 * 子商户appid
	 */
	public static String SUB_APPID="wxfd9b70fbe295841d";

	/**
	 * 微信支付
	 */
	public static String AppSecret= "ca66f91ef5b24cc7b34514dfe41c9e8c";
	/**
	 * 异步通知地址
	 */
	public static String NOTIFY_URL = ConfigUtil.getSingleValue("subwxBackUrl"); 
									
	/** 
	 * 调取微信统一支付 url
	 */
	public static String WX_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
}
