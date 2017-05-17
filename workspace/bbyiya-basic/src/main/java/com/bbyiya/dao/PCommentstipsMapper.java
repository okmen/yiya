package com.bbyiya.dao;

import com.bbyiya.model.PCommentstips;

public interface PCommentstipsMapper {
    int deleteByPrimaryKey(Integer tipid);

    int insert(PCommentstips record);

    int insertSelective(PCommentstips record);

    PCommentstips selectByPrimaryKey(Integer tipid);

    int updateByPrimaryKeySelective(PCommentstips record);

    int updateByPrimaryKey(PCommentstips record);
}