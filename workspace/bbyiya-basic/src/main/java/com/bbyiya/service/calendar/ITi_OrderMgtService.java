package com.bbyiya.service.calendar;

import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.order.UserOrderSubmitParam;

public interface ITi_OrderMgtService {

	/**
	 * 用户下单（c端）
	 * @param param
	 * @return
	 */
	public ReturnModel submitOrder(UserOrderSubmitParam param);
}
