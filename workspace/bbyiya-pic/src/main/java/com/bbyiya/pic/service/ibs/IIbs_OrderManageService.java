package com.bbyiya.pic.service.ibs;

import com.bbyiya.vo.ReturnModel;

public interface IIbs_OrderManageService {

	/**
	 * 根据推荐userId获取订单列表
	 * @param userId
	 * @param status
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel find_payorderExtByUpUserid(Long userId,Integer status, int index,int size);
}
