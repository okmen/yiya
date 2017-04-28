package com.bbyiya.dao;

import com.bbyiya.model.UBranches;

public interface UBranchesMapper {
    int deleteByPrimaryKey(Long branchuserid);

    int insert(UBranches record);

    int insertSelective(UBranches record);

    UBranches selectByPrimaryKey(Long branchuserid);

    int updateByPrimaryKeySelective(UBranches record);

    int updateByPrimaryKey(UBranches record);
}