package com.bbyiya.dao;

import com.bbyiya.model.UBranchinfotemp;

public interface UBranchinfotempMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UBranchinfotemp record);

    int insertSelective(UBranchinfotemp record);

    UBranchinfotemp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UBranchinfotemp record);

    int updateByPrimaryKey(UBranchinfotemp record);
}