package com.bbyiya.dao;

import com.bbyiya.model.RAreas;

public interface RAreasMapper {
    int deleteByPrimaryKey(Integer code);

    int insert(RAreas record);

    int insertSelective(RAreas record);

    RAreas selectByPrimaryKey(Integer code);

    int updateByPrimaryKeySelective(RAreas record);

    int updateByPrimaryKey(RAreas record);
}