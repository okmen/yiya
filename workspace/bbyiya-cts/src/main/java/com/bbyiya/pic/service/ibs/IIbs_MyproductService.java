package com.bbyiya.pic.service.ibs;

import com.bbyiya.vo.ReturnModel;

public interface IIbs_MyproductService {
	/**
	 * 影楼员工协助客户的作品列表
	 * @param branchUserId
	 * @param status
	 * @param inviteStatus
	 * @param keywords
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findMyProductsSourceCustomerOfBranch(Long branchUserId,
			Integer status, Integer inviteStatus, String keywords, int index,
			int size);
	
	
}
