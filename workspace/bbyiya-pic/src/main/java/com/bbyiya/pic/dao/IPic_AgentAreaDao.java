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
}
