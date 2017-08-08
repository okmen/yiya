package com.bbyiya.cts.service;

import com.bbyiya.vo.ReturnModel;


public interface ITempAutoOrderSumbitService {
	/**
	 * 为参与活动的达到条件的作品自动下单
	 * @param model
	 * @return
	 */
	ReturnModel dotempAutoOrderSumbit();
	
	/**
	 * 自动签收订单
	 * @return
	 */
	void doAutoReceiving();
	
}
