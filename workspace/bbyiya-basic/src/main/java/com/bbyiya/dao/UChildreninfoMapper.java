package com.bbyiya.dao;

import com.bbyiya.model.UChildreninfo;

public interface UChildreninfoMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UChildreninfo record);

    int insertSelective(UChildreninfo record);

    UChildreninfo selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UChildreninfo record);

    int updateByPrimaryKey(UChildreninfo record);
}