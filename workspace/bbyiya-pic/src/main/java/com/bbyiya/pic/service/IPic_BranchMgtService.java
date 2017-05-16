package com.bbyiya.pic.service;

import com.bbyiya.model.UAgentapply;
import com.bbyiya.model.UBranches;
import com.bbyiya.pic.vo.agent.AgentSearchParam;
import com.bbyiya.pic.vo.agent.UBranchVo;
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
	
	ReturnModel findAgentApplyList(AgentSearchParam param);
	
	ReturnModel findBranchVoList(AgentSearchParam param,int index, int size);
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
	
	ReturnModel getAgentApplyStatusModel(Long agentUserId);
	
	ReturnModel getBranchApplyStatusModel(Long agentUserId);
	/**
	 * ��ȡ��������Ϣ
	 * @param branchUserId
	 * @return
	 */
	UBranchVo getBranchInfo(Long branchUserId);
	/**
	 * �޸Ĵ������ջ���ַ
	 * @param branchUserId
	 * @param streetdetail
	 * @return
	 */
	ReturnModel editBranchAddress(Long branchUserId, String streetdetail,String name,String phone);
	/**
	 * ����������
	 * @param branchUserId
	 * @param content
	 * @return
	 */
	ReturnModel addUserResponses(Long branchUserId, String content);
	/**
	 * IBS��ȡϵͳ��Ϣ֪ͨ�б�
	 * @param index
	 * @param size
	 * @param startTimeStr
	 * @param endTimeStr
	 * @return ReturnModel 
	 */
	ReturnModel getSysMessageList(int index, int size, String startTimeStr,
			String endTimeStr);
	/**
	 * ��������פ
	 * @param adminId
	 * @param agentUserId
	 * @return
	 */
	ReturnModel agentTuiZhu(String adminname,Long adminId, Long agentUserId);
}
