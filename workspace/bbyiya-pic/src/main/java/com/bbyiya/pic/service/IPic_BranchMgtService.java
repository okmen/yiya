package com.bbyiya.pic.service;

import com.bbyiya.model.UAgentapply;
import com.bbyiya.model.UBranches;
import com.bbyiya.vo.ReturnModel;

public interface IPic_BranchMgtService {

	/**
	 * ���������жϴ������
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 */
	ReturnModel getBranchAreaPrice(Integer province,Integer city,Integer district);
	/**
	 * ����������
	 * @param userId
	 * @param applyInfo
	 * @return
	 */
	ReturnModel applyAgent(Long userId,UAgentapply applyInfo);
	/**
	 * Ӱ¥�ֵ�����
	 * @param userId
	 * @param applyInfo
	 * @return
	 */
	ReturnModel applyBranch(Long userId,UBranches applyInfo);
	/**
	 * ���������
	 * @param adminId
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 */
	ReturnModel audit_AgentApply(Long adminId,Long agentUserId,int status,String msg);
	/**
	 * Ӱ¥�ֵ����
	 * @param adminId
	 * @param branchUserId
	 * @param status
	 * @param msg
	 * @return
	 */
	ReturnModel audit_BranchApply(Long adminId,Long branchUserId,int status,String msg);
	
	/**
	 * ���������ѯ
	 * @param areaCode
	 * @return
	 */
	ReturnModel getAgentArea(Integer areaCode);
}
