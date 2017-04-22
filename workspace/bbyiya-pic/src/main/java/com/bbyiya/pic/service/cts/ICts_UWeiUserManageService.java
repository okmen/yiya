package com.bbyiya.pic.service.cts;

import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.UWeiUserSearchParam;

public interface ICts_UWeiUserManageService {

	/**
	 * 获取影楼推荐人发展的用户订单列表
	 * @param branchuserId 影楼ID
	 * @param startTimeStr 开始时间
	 * @param endTimeStr	结束时间
	 * @param status	状态
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel find_payorderExtByBranchUpUserid(Long branchuserId,String startTimeStr,String endTimeStr,Integer status, int index,int size);
	/**
	 * 获取微商列表
	 * @param param
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findWeiUserVoList(UWeiUserSearchParam param, int index, int size);
}
