package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.UBranchusers;

public interface UBranchusersMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UBranchusers record);

    int insertSelective(UBranchusers record);

    UBranchusers selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UBranchusers record);

    int updateByPrimaryKey(UBranchusers record);
    /**
     * 获取影楼的内部员工列表
     * @param branchUserId
     * @return
     */
    List<UBranchusers> findMemberslistByBranchUserId(@Param("branchUserId") Long branchUserId);
    /**
     * 通过代理商userId获取所有员工列表
     * @param agentUserId
     * @return
     */
    List<UBranchusers> findMemberslistByAgentUserId(@Param("agentUserId") Long agentUserId);
}