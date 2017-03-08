package com.bbyiya.pic.service;

import com.bbyiya.model.UBranchusers;
import com.bbyiya.vo.ReturnModel;

public interface IPic_MemberMgtService {
	/**
	 * 获取影楼的内部员工列表
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findBranchUserslistByBranchUserId(Long branchUserId);
	/**
	 * 添加影楼的 销售人员
	 * @param branchUserId
	 * @param param
	 * @return
	 */
	ReturnModel addBranchUser(Long branchUserId,UBranchusers param);
	
	ReturnModel delBranchUser(Long branchUserId,Long userId);
}
