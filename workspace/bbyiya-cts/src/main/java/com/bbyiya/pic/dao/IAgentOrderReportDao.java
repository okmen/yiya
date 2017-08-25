package com.bbyiya.pic.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.order.BranchOrderReportVO;

public interface IAgentOrderReportDao {

	Integer getBranchCountByagentUserId(@Param("agentUserId") Long agentUserId);
	
	List<BranchOrderReportVO> findBranchOrderReportVo(@Param("agentUserId") Long agentUserId,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

}
