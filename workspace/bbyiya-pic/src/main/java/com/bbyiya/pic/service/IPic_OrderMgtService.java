package com.bbyiya.pic.service;

import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.vo.ReturnModel;

public interface IPic_OrderMgtService {

	/**
	 * ��ѯ�����б�
	 * @param param
	 * @return
	 */
	ReturnModel find_orderList(SearchOrderParam param);
}
