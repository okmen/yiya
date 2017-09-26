package com.bbyiya.service;

import com.bbyiya.enums.calendar.TiAmountLogType;

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
	
	/**
	 * 台历挂历 订单金额分配
	 * ps :用户确认收货 后分利
	 * @param userOrderId
	 */
	void distributeOrderAmount(String userOrderId);
	/**
	 * 插入台历 交易流水
	 * @param payId
	 * @param userid
	 * @param amount
	 * @param type
	 */
	void add_tiAccountLog(String payId,long userid, double amount,TiAmountLogType type);
}
