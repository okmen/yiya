package com.bbyiya.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.api.service.IPayService;
import com.bbyiya.vo.ReturnModel;

@Service("apiPayService")
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class PayService implements IPayService{

	public ReturnModel getWeiXinPay_Param(long userId, String orderNo, String ipAddress){
		return null;
	}
}
