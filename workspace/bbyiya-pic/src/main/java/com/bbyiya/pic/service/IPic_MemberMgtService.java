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
	/**
	 * ɾ�����пͻ�
	 * @param branchUserId
	 * @param customerId
	 * @return
	 */
	ReturnModel deleteCustomer(Long branchUserId,Long customerId);
	/**
	 * ����AgentUserId�õ�Uagentcustomers�б�  
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findCustomerslistByAgentUserId(Long branchUserId,String keywords,int index,int size);
	/**
	 * ����UserId�õ��ͻ��Ĺ����б�
	 * @param userId
	 * @return
	 */
	ReturnModel findCustomersBuylistByUserId(Long userId);
	/**
	 * ����BranchUserId�õ��ѻ�ȡ�ͻ��б� 
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findMarketCustomerslistByBranchUserId(Long branchUserId,String keywords,int index,int size);
}
