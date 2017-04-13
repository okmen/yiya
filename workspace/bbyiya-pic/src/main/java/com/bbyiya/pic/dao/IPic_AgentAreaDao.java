package com.bbyiya.pic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.RAreaplans;

public interface IPic_AgentAreaDao {

	/**
	 * 通过代理单元获取 代理区县
	 * @param areaId
	 * @return
	 */
	List<RAreaplans> findRAreaplansByAreaId(@Param("areaId") Integer areaId);
	
	/**
	 * 通过代理商ID获取代理区县
	 * @param agentUserId
	 * @return
	 */
	List<RAreaplans> findRAreaplansByAgentUserId(@Param("agentUserId") Long agentUserId);
}
