package com.bbyiya.pic.service;

import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.vo.ReturnModel;

public interface IPic_MemberMgtService {
	
	/*----------------------------Ӱ¥�ڲ�Ա������ ģ��-----------------------------------------------*/
	/**
	 * ��ȡӰ¥���ڲ�Ա���б�
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findBranchUserslistByBranchUserId(Long branchUserId);
	/**
	 * ���Ӱ¥�� ������Ա
	 * @param branchUserId
	 * @param param
	 * @return
	 */
	ReturnModel addBranchUser(Long branchUserId,UBranchusers param);
	/**
	 * ɾ��Ӱ¥�ڲ�Ա����Ϣ
	 * @param branchUserId
	 * @param userId
	 * @return
	 */
	ReturnModel delBranchUser(Long branchUserId,Long userId);
	
	/*--------------------------Ӱ¥�ͻ�����ģ��--------------------------------------------------------------------*/
	/**
	 * ��ȡӰ¥�Ŀͻ��б�
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findCustomersByBranchUserId(Long branchUserId);
	/**
	 * �޸Ŀͻ�����
	 * @param branchUserId
	 * @param param
	 * @return
	 */
	ReturnModel editCustomer(Long branchUserId,UAgentcustomers param);
	/**
	 * �����ͻ�
	 * @param branchUserId
	 * @param param
	 * @return
	 */
	ReturnModel addCustomer(Long branchUserId,UAgentcustomers param);
}
