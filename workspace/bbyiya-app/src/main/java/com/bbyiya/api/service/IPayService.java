package com.bbyiya.api.service;

import com.bbyiya.vo.ReturnModel;

public interface IPayService {

	/**
	 * 获取微信支付参数
	 * @param userId
	 * @param orderNo
	 * @param ipAddress
	 * @return
	 */
	ReturnModel getWeiXinPay_Param(long userId, String orderNo, String ipAddress);
}
