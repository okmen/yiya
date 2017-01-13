package com.bbyiya.utils.pay;

import com.bbyiya.utils.ConfigUtil;

/**
 * 咿呀科技  开放平台
 * @author Administrator
 *
 */
public class WxPayAppConfig {
	/**
	 * 商户号
	 */
	public static String PARNER = "1427265002"; //
	/**
	 * 微信分配的公众账号ID
	 */
	public static String APPID = "wxfcd7fab4005cb1ee"; //

	public static String AppSecret= "ca66f91ef5b24cc7b34514dfe41c9e8c";
	/**
	 * 异步通知地址
	 */
	public static String NOTIFY_URL = ConfigUtil.getSingleValue("wxBackUrl"); 
									
	/** 
	 * 调取微信统一支付 url
	 */
	public static String WX_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
}
