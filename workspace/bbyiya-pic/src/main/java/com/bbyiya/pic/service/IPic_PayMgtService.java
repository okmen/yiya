package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;

public interface IPic_PayMgtService {

	/**
	 * ��ȡ֧��������΢�Ź��ںţ�
	 * 
	 * @param orderNo
	 * @param openid
	 * @param ip
	 * @return
	 */
	ReturnModel getWxPayParam(String orderNo, String openid, String ip);

	/**
	 * ֧��������΢��APP��
	 * 
	 * @param orderNo
	 * @param ip
	 * @return
	 */
	ReturnModel getWxAppPayParam(String orderNo, String ip);
}
