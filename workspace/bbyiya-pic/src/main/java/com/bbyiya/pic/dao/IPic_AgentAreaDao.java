package com.bbyiya.pic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.RAreaplans;

public interface IPic_AgentAreaDao {

	/**
	 * ͨ������Ԫ��ȡ ��������
	 * @param areaId
	 * @return
	 */
	List<RAreaplans> findRAreaplansByAreaId(@Param("areaId") Integer areaId);
	
	/**
	 * ͨ��������ID��ȡ��������
	 * @param agentUserId
	 * @return
	 */
	List<RAreaplans> findRAreaplansByAgentUserId(@Param("agentUserId") Long agentUserId);
}
