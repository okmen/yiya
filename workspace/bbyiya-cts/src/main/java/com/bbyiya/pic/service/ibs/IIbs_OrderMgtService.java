package com.bbyiya.pic.service.ibs;

import com.bbyiya.vo.ReturnModel;

public interface IIbs_OrderMgtService {
	/**
	 * 得到来自分店的订单列表
	 * @param branchUserId
	 * @param status
	 * @param keywords
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findMyBranchOrderlist(Long branchUserId, Integer status,
			String keywords, int index, int size);
	
}
