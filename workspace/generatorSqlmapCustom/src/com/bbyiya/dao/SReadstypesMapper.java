package com.bbyiya.dao;

import com.bbyiya.model.SReadstypes;

public interface SReadstypesMapper {
    int deleteByPrimaryKey(Integer readtypeid);

    int insert(SReadstypes record);

    int insertSelective(SReadstypes record);

    SReadstypes selectByPrimaryKey(Integer readtypeid);

    int updateByPrimaryKeySelective(SReadstypes record);

    int updateByPrimaryKey(SReadstypes record);
}