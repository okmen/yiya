package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.vo.agent.UAgentcustomersVo;

public interface UAgentcustomersMapper {
    int deleteByPrimaryKey(Long customerid);

    int insert(UAgentcustomers record);

    int insertSelective(UAgentcustomers record);

    UAgentcustomers selectByPrimaryKey(Long customerid);

    int updateByPrimaryKeySelective(UAgentcustomers record);

    int updateByPrimaryKey(UAgentcustomers record);
    /**
     * 根据branchUserId获取影楼的用户列表
     * @param branchUserId
     * @return
     */
    List<UAgentcustomers> findCustomersByBranchUserId(@Param("branchUserId") Long branchUserId);
    
    /**
     * 根据AgentUserId获取影楼的用户列表
     * @param branchUserId
     * @return
     */
    List<UAgentcustomersVo> findCustomersByAgentUserId(@Param("agentUserId") Long agentUserId);
    
    /**
     * 根据代理userId， 买家Userid
     * @param agentUserId
     * @param userId
     * @return
     */
    UAgentcustomers getCustomersByAgentUserId(@Param("agentUserId") Long agentUserId ,@Param("userId") Long userId);
}