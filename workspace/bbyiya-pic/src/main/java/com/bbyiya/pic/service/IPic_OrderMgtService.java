package com.bbyiya.pic.service;

import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.vo.ReturnModel;

public interface IPic_OrderMgtService {

	/**
	 * 查询订单列表（CTS用）
	 * 
	 * @param param
	 * @return
	 */
	ReturnModel find_orderList(SearchOrderParam param);

	/**
	 * 获取待分配的订单（IBS用）
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findAgentOrders(Long branchUserId);
}
