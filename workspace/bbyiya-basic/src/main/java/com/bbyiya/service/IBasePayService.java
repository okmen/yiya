package com.bbyiya.service;

public interface IBasePayService {

	/**
	 * 微信支付回写
	 * @param payId
	 * @return
	 */
	boolean paySuccessProcess(String payId);
	
	/**
	 * 产品销量
	 * @param userOrderId
	 */
	void addProductExt(String userOrderId);
}
