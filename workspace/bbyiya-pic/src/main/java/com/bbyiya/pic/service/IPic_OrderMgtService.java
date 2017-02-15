package com.bbyiya.pic.service;

import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.vo.ReturnModel;

public interface IPic_OrderMgtService {

	/**
	 * 查询订单列表
	 * @param param
	 * @return
	 */
	ReturnModel find_orderList(SearchOrderParam param);
}
