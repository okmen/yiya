package com.bbyiya.utils.pay.config;

import com.bbyiya.utils.ConfigUtil;

public class SubWxPayConfig {
	/**
	 * 商户号(幻想馆) ps:微信支付分配的商户号
	 */
	public static String MCH_ID = "1488843162"; 
	/**
	 * 微信支付分配的子商户号
	 */
	public	static String SUB_MCH_ID="1488843162";
	/**
	 * 微信分配的公众账号ID
	 */
	public static String APPID = "wxfcd7fab4005cb1ee"; //

	/**
	 * 微信支付
	 */
	public static String AppSecret= "ca66f91ef5b24cc7b34514dfe41c9e8c";
	/**
	 * 异步通知地址
	 */
	public static String NOTIFY_URL = ConfigUtil.getSingleValue("wxAppBackUrl"); 
									
	/** 
	 * 调取微信统一支付 url
	 */
	public static String WX_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
}
