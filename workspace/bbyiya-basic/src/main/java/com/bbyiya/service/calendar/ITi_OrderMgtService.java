package com.bbyiya.service.calendar;

import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiActivityOrderSubmitParam;
import com.bbyiya.vo.order.UserOrderSubmitParam;

public interface ITi_OrderMgtService {

	/**
	 * 用户下单（c端）
	 * @param param
	 * @return
	 */
	ReturnModel submitOrder(UserOrderSubmitParam param);
	/**
	 * 活动下单（ibs下单）
	 * @param param
	 * @return
	 */
	ReturnModel submitOrder_ibs(TiActivityOrderSubmitParam param);
	
	long getOrderAddressId(Long userAddressId);
}
