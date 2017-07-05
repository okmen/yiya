package com.bbyiya.pic.service;

import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.vo.ReturnModel;

public interface IPic_MemberMgtService {
	
	/*----------------------------影楼内部员工管理 模块-----------------------------------------------*/
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
	/**
	 * 删除影楼内部员工信息
	 * @param branchUserId
	 * @param userId
	 * @return
	 */
	ReturnModel delBranchUser(Long branchUserId,Long userId);
	
	/*--------------------------影楼客户管理模块--------------------------------------------------------------------*/
	
	/**
	 * 修改客户资料
	 * @param branchUserId
	 * @param param
	 * @return
	 */
	ReturnModel editCustomer(Long branchUserId,UAgentcustomers param);
	/**
	 * 新增客户
	 * @param branchUserId
	 * @param param
	 * @return
	 */
	ReturnModel addCustomer(Long branchUserId,UAgentcustomers param);
	/**
	 * 删除已有客户
	 * @param branchUserId
	 * @param customerId
	 * @return
	 */
	ReturnModel deleteCustomer(Long branchUserId,Long customerId);
	/**
	 * 根据AgentUserId得到Uagentcustomers列表  
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findCustomerslistByAgentUserId(Long branchUserId,String keywords,int index,int size);
	/**
	 * 根据UserId得到客户的购买列表
	 * @param userId
	 * @return
	 */
	ReturnModel findCustomersBuylistByUserId(Long userId);
	/**
	 * 根据BranchUserId得到已获取客户列表 
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findMarketCustomerslistByBranchUserId(Long branchUserId,String keywords,int index,int size);
}
