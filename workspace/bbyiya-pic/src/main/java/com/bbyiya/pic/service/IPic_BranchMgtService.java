package com.bbyiya.pic.service;

import com.bbyiya.model.UAgentapply;
import com.bbyiya.model.UBranches;
import com.bbyiya.pic.vo.agent.AgentSearchParam;
import com.bbyiya.pic.vo.agent.UBranchVo;
import com.bbyiya.vo.ReturnModel;

public interface IPic_BranchMgtService {

	/**
	 * 根据区域判断代理费用
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 */
	ReturnModel getBranchAreaPrice(Integer province,Integer city,Integer district);
	/**
	 * 代理商申请
	 * @param userId
	 * @param applyInfo
	 * @return
	 */
	ReturnModel applyAgent(Long userId,UAgentapply applyInfo);
	
	ReturnModel findAgentApplyList(AgentSearchParam param);
	
	ReturnModel findBranchVoList(AgentSearchParam param,int index, int size);
	/**
	 * 影楼分店申请
	 * @param userId
	 * @param applyInfo
	 * @return
	 */
	ReturnModel applyBranch(Long userId,UBranches applyInfo);
	/**
	 * 代理商审核
	 * @param adminId
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 */
	ReturnModel audit_AgentApply(Long adminId,Long agentUserId,int status,String msg);
	
	/**
	 * 影楼分店审核
	 * @param adminId
	 * @param branchUserId
	 * @param status
	 * @param msg
	 * @return
	 */
	ReturnModel audit_BranchApply(Long adminId,Long branchUserId,int status,String msg);
	
	/**
	 * 代理区域查询
	 * @param areaCode
	 * @return
	 */
	ReturnModel getAgentArea(Integer areaCode);
	
	ReturnModel getAgentApplyStatusModel(Long agentUserId);
	
	ReturnModel getBranchApplyStatusModel(Long agentUserId);
	/**
	 * 获取代理商信息
	 * @param branchUserId
	 * @return
	 */
	UBranchVo getBranchInfo(Long branchUserId);
	/**
	 * 修改代理商收货地址
	 * @param branchUserId
	 * @param streetdetail
	 * @return
	 */
	ReturnModel editBranchAddress(Long branchUserId, String streetdetail,String name,String phone);
	/**
	 * 添加意见反馈
	 * @param branchUserId
	 * @param content
	 * @return
	 */
	ReturnModel addUserResponses(Long branchUserId, String content);
	/**
	 * IBS获取系统消息通知列表
	 * @param index
	 * @param size
	 * @param startTimeStr
	 * @param endTimeStr
	 * @return ReturnModel 
	 */
	ReturnModel getSysMessageList(int index, int size, String startTimeStr,
			String endTimeStr);
	/**
	 * 代理商退驻
	 * @param adminId
	 * @param agentUserId
	 * @return
	 */
	ReturnModel agentTuiZhu(String adminname,Long adminId, Long agentUserId);
}
