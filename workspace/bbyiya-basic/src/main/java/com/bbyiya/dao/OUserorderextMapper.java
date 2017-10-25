package com.bbyiya.dao;

import com.bbyiya.model.OUserorderext;

public interface OUserorderextMapper {
    int deleteByPrimaryKey(String userorderid);

    int insert(OUserorderext record);

    int insertSelective(OUserorderext record);

    OUserorderext selectByPrimaryKey(String userorderid);

    int updateByPrimaryKeySelective(OUserorderext record);

    int updateByPrimaryKey(OUserorderext record);
}