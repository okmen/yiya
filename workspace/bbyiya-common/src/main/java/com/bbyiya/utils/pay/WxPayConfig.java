package com.bbyiya.utils.pay;

import com.bbyiya.utils.ConfigUtil;
/**
 * 咿呀科技（公众号支付）
 * @author Administrator
 *
 */
public class WxPayConfig {
	/**
	 * 商户号
	 */
	public static String PARNER = "1427250602";//"1427265002"; //
	/**
	 * 微信分配的公众账号ID
	 */
	public static String APPID = "wxcc101e7b17ed868e";//"wxfcd7fab4005cb1ee"; //
	public static String AppSecret= "af5357b12453ed6a0ae6aba1eb64c3dd";//
	//测试号
//	public static String APPID ="wxa325507a68155be8";
//	//测试key
//	public static String AppSecret= "e2a513b903394bde1ed2da65b365a1cb";//"af5357b12453ed6a0ae6aba1eb64c3dd";// "8cdfc6f23d3b8e99dd596cb377b7bca4";
	/**
	 * 异步通知地址
	 */
	public static String NOTIFY_URL = ConfigUtil.getSingleValue("wxBackUrl"); 
									
	/** 
	 * 调取微信统一支付 url
	 */
	public static String WX_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
}
