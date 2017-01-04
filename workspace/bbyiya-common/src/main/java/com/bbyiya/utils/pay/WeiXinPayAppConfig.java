package com.bbyiya.utils.pay;

import com.bbyiya.utils.ConfigUtil;

public class WeiXinPayAppConfig {
	/**
	 * 商户号
	 */
	public static String PARNER = "1427265002"; 
	/**
	 * 微信分配的公众账号ID
	 */
	public static String APPID = "wxfcd7fab4005cb1ee"; //

	// key
//	public static String APPKEY = "Yqz3GDIJbISJkMHNMrDJnZk3sgSrdjRFe25xYHZJUcXndHgvCmEgZ8RKyrrYNLk3FAOilEheSySlgwjP5AavKgCy01nrQQT0h3wX1R6t7ipXKOw0PKdwBxPrqyRsXnOJ";// paysignkey(非appkey)
	public static String APP_SIGN_KEY= "EE0CEE7DD4EDF67CDF51A419814C80E3";
	/**
	 * 异步通知地址
	 */
	public static String NOTIFY_URL = ConfigUtil.getSingleValue("wxBackUrl"); 
									
	/** 
	 * 调取微信统一支付 url
	 */
	public static String WX_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
}
