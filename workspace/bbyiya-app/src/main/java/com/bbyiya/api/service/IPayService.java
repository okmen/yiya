package com.bbyiya.api.service;

import com.bbyiya.vo.ReturnModel;

public interface IPayService {

	/**
	 * ��ȡ΢��֧������
	 * @param userId
	 * @param orderNo
	 * @param ipAddress
	 * @return
	 */
	ReturnModel getWeiXinPay_Param(long userId, String orderNo, String ipAddress);
}
