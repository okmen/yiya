package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.UAgentcustomers;

public interface UAgentcustomersMapper {
    int deleteByPrimaryKey(Long customerid);

    int insert(UAgentcustomers record);

    int insertSelective(UAgentcustomers record);

    UAgentcustomers selectByPrimaryKey(Long customerid);

    int updateByPrimaryKeySelective(UAgentcustomers record);

    int updateByPrimaryKey(UAgentcustomers record);
    /**
     * 获取影楼的用户列表
     * @param branchUserId
     * @return
     */
    List<UAgentcustomers> findCustomersByBranchUserId(@Param("branchUserId") Long branchUserId);
}