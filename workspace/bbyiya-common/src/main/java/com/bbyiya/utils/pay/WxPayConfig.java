package com.bbyiya.utils.pay;

import com.bbyiya.utils.ConfigUtil;

public class WxPayConfig {
	/**
	 * 商户号
	 */
	public static String PARNER = "1427250602";//"1427265002"; //
	/**
	 * 微信分配的公众账号ID
	 */
	public static String APPID = "wxcc101e7b17ed868e";//"wxfcd7fab4005cb1ee"; //

	// key
//	public static String APPKEY = "Yqz3GDIJbISJkMHNMrDJnZk3sgSrdjRFe25xYHZJUcXndHgvCmEgZ8RKyrrYNLk3FAOilEheSySlgwjP5AavKgCy01nrQQT0h3wX1R6t7ipXKOw0PKdwBxPrqyRsXnOJ";// paysignkey(非appkey)
	public static String AppSecret= "af5357b12453ed6a0ae6aba1eb64c3dd";// "8cdfc6f23d3b8e99dd596cb377b7bca4";
	/**
	 * 异步通知地址
	 */
	public static String NOTIFY_URL = ConfigUtil.getSingleValue("wxBackUrl"); 
									
	/** 
	 * 调取微信统一支付 url
	 */
	public static String WX_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
}
