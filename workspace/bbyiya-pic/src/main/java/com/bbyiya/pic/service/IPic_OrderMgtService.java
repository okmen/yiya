package com.bbyiya.pic.service;

import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.vo.ReturnModel;

public interface IPic_OrderMgtService {

	/**
	 * ��ѯ�����б�CTS�ã�
	 * 
	 * @param param
	 * @return
	 */
	ReturnModel find_orderList(SearchOrderParam param);

	/**
	 * ��ȡ������Ķ�����IBS�ã�
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findAgentOrders(Long branchUserId);
}
