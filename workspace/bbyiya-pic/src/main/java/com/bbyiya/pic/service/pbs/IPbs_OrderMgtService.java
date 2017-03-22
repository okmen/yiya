package com.bbyiya.pic.service.pbs;

import com.bbyiya.pic.vo.order.PbsSearchOrderParam;
import com.bbyiya.vo.ReturnModel;

public interface IPbs_OrderMgtService {

	/**
	 * 查询订单列表（pbs用）
	 * 
	 * @param param
	 * @return
	 */
	ReturnModel find_pbsOrderList(PbsSearchOrderParam param);

	
}
