package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;

public interface IPic_PayMgtService {

	/**
	 * 获取支付参数（微信公众号）
	 * 
	 * @param orderNo
	 * @param openid
	 * @param ip
	 * @return
	 */
	ReturnModel getWxPayParam(String orderNo, String openid, String ip);

	/**
	 * 支付参数（微信APP）
	 * 
	 * @param orderNo
	 * @param ip
	 * @return
	 */
	ReturnModel getWxAppPayParam(String orderNo, String ip);
	
	ReturnModel getWxCode_url(String orderNo, String ip);
	
	/**
	 * 微信支付到 幻想馆
	 * @param orderNo
	 * @param openid
	 * @param ip
	 * @return
	 */
	ReturnModel getSubWxPayParam(String orderNo, String openid, String ip);
}
