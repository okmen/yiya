package com.bbyiya.dao;

import com.bbyiya.model.OUserorders;

public interface OUserordersMapper {
    int deleteByPrimaryKey(String userorderid);

    int insert(OUserorders record);

    int insertSelective(OUserorders record);

    OUserorders selectByPrimaryKey(String userorderid);

    int updateByPrimaryKeySelective(OUserorders record);

    int updateByPrimaryKey(OUserorders record);
}