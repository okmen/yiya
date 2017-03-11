package com.bbyiya.pic.dao;

import java.util.List;

import com.bbyiya.pic.vo.agent.AgentSearchParam;
import com.bbyiya.pic.vo.agent.UAgentApplyVo;
import com.bbyiya.pic.vo.agent.UBranchVo;

public interface IPic_AgentMgtDao {

	/**
	 * ��ȡ������������Ϣ
	 * @param agentuserid
	 * @return
	 */
	UAgentApplyVo getUAgentapplyVOByAgentUserId(Long agentuserid);
	/**
	 * ��ȡӰ¥��Ϣ
	 * @param branchuserid
	 * @return
	 */
	UBranchVo getUBranchVoByBranchUserId(Long branchuserid);
	
	List<UAgentApplyVo> findUAgentapplyVOList(AgentSearchParam param); 
	/**
	 * ��ȡӰ¥�б�
	 * @param param
	 * @return
	 */
	List<UBranchVo> findUBranchVoList(AgentSearchParam param);
}
