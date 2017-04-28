package com.bbyiya.dao;

import com.bbyiya.model.PStylecoordinateitem;

public interface PStylecoordinateitemMapper {
    int deleteByPrimaryKey(Long coordid);

    int insert(PStylecoordinateitem record);

    int insertSelective(PStylecoordinateitem record);

    PStylecoordinateitem selectByPrimaryKey(Long coordid);

    int updateByPrimaryKeySelective(PStylecoordinateitem record);

    int updateByPrimaryKey(PStylecoordinateitem record);
}