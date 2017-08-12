package com.bbyiya.dao;

import com.bbyiya.model.OBranchorders;

public interface OBranchordersMapper {
    int deleteByPrimaryKey(String userorderid);

    int insert(OBranchorders record);

    int insertSelective(OBranchorders record);

    OBranchorders selectByPrimaryKey(String userorderid);

    int updateByPrimaryKeySelective(OBranchorders record);

    int updateByPrimaryKey(OBranchorders record);
}