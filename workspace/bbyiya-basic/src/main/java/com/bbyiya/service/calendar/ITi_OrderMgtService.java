package com.bbyiya.service.calendar;

import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.address.OrderaddressParam;
import com.bbyiya.vo.calendar.TiActivityOrderSubmitParam;
import com.bbyiya.vo.calendar.TiGroupActivityOrderSubmitParam;
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
	/**
	 * 生成订单收货地址 返回订单收货地址Id
	 * @param userAddressId(用户收货地址Id)
	 * @return 订单收货地址Id
	 */
	long addOrderAddressReturnId(Long userAddressId);
	/**
	 * 代客制作自动下单
	 * @param param
	 * @return
	 */
	ReturnModel submitTiCustomerOrder_ibs(TiActivityOrderSubmitParam param,OrderaddressParam addressParam);
	/**
	 * 分销下单
	 * @param param
	 * @return
	 */
	ReturnModel submitTiGroupActivityOrder_ibs(TiGroupActivityOrderSubmitParam param);
}
