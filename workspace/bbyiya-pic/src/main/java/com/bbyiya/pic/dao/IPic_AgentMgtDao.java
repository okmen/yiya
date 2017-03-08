package com.bbyiya.pic.dao;

import com.bbyiya.pic.vo.agent.UAgentApplyVo;
import com.bbyiya.pic.vo.agent.UBranchVo;

public interface IPic_AgentMgtDao {

	/**
	 * 获取代理商申请信息
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
}
