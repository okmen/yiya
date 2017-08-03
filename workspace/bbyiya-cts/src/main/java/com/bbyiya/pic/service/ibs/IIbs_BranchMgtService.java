package com.bbyiya.pic.service.ibs;

import com.bbyiya.vo.ReturnModel;

public interface IIbs_BranchMgtService {
	/**
	 * 修改代理商店铺页信息
	 * @param branchUserId
	 * @param logo
	 * @param promotionstr
	 * @return
	 */
	ReturnModel editBranchShopInfo(Long branchUserId, String logo,
			String promotionstr);
	
	
}
