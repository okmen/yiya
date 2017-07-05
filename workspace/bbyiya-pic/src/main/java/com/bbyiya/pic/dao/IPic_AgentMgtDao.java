package com.bbyiya.pic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.agent.AgentSearchParam;
import com.bbyiya.pic.vo.agent.UAgentApplyVo;
import com.bbyiya.pic.vo.agent.UBranchVo;

public interface IPic_AgentMgtDao {

	/**
	 *  获取代理商申请信息
	 * @param agentuserid
	 * @return
	 */
	UAgentApplyVo getUAgentapplyVOByAgentUserId(Long agentuserid);
	/**
	 * 获取影楼信息
	 * @param branchuserid
	 * @return
	 */
	UBranchVo getUBranchVoByBranchUserId(Long branchuserid);
	
	List<UAgentApplyVo> findUAgentapplyVOList(AgentSearchParam param); 
	/**
	 * 获取影楼列表
	 * @param param
	 * @return
	 */
	List<UBranchVo> findUBranchVoList(AgentSearchParam param);
	/**
	 * 根据代理商ID获取影楼列表
	 * @param agentUserId
	 * @return
	 */
	List<UBranchVo> findUBranchVoListByAgentUserId(@Param("agentUserId") Long agentUserId);
}
