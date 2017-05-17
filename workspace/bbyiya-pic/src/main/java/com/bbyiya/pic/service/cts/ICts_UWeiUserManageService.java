package com.bbyiya.pic.service.cts;

import com.bbyiya.model.UWeiuserapplys;
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
	/**
	 * 流量主申请
	 * @param userId
	 * @param applyInfo
	 * @return
	 */
	ReturnModel applyWeiUser(Long userId, UWeiuserapplys applyInfo);
	/**
	 * 流量主审核
	 * @param adminId
	 * @param weiUserId
	 * @param status
	 * @return
	 */
	ReturnModel audit_weiUserApply(Long adminId, Long weiUserId, int status);
	/**
	 * 查询流量主申请列表
	 * @param param
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findWeiUserApplylist(UWeiUserSearchParam param, int index,
			int size);
	/**
	 * 删除流量主记录
	 * @param adminId
	 * @param weiUserId
	 * @return
	 */
	ReturnModel delete_weiUserApply(Long adminId, Long weiUserId);
}
