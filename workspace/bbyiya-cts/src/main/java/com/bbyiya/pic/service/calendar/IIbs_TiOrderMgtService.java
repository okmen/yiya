package com.bbyiya.pic.service.calendar;

import com.bbyiya.vo.ReturnModel;

public interface IIbs_TiOrderMgtService {
	/**
	 * 得到来自分店的订单列表
	 * @param branchUserId
	 * @param status
	 * @param keywords
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findTiMyOrderlist(Long branchUserId,Integer ordertype, Integer status,
			String keywords, int index, int size);
	
	
}