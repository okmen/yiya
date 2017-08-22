package com.bbyiya.pic.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.AgentDateVO;

public interface IPic_DataTempDao {

	/**
	 * 清除表 Pagentmyproducttempview
	 */
	void clearPagentmyproducttempview();
	/**
	 * 数据总览列表 cts
	 * @param type(1:参与人降序，2参与人升序，3完成人降序4完成人升序，5下单数降序6下单数升序 7作品数降序8作品数升序 9代理商userId降序，0代理商userid升序)
	 * @param branchcompanyname 代理商查询（可以是userId/公司名称：ps分公司名称）
	 * @return
	 */
	List<AgentDateVO> findActslist(@Param("type") Integer type,@Param("branchcompanyname")String companyName);
	/**
	 * 异业活动申请情况统计（代理商活动 ）
	 * @param agentUserId
	 * @return
	 */
	List<AgentDateVO> findMyProductTempVo(@Param("agentUserId") Long agentUserId,@Param("startTime")Date startTime,@Param("endTime")Date endTime);
	/**
	 * 参与异业活动完成情况统计（代理商）
	 * @param agentUserId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<AgentDateVO> findMyProductTempCompleteVo(@Param("agentUserId") Long agentUserId,@Param("startTime") Date startTime,@Param("endTime") Date endTime);
	/**
	 * 代理商订单情况
	 * @param agentUserId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<AgentDateVO> findOrderVO(@Param("agentUserId") Long agentUserId,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

	/**
	 * 被邀请创作的作品统计（代理商客户 ）
	 * @param agentUserId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<AgentDateVO> findInviteMycartNewlist(@Param("agentUserId") Long agentUserId,@Param("startTime") Date startTime,@Param("endTime") Date endTime);
	/**
	 * 用户自己创作的作品统计（代理商的推广用户 ）
	 * @param agentUserId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<AgentDateVO> findSelfMycartNewlist(@Param("agentUserId") Long agentUserId,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

}
