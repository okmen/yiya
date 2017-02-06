package com.bbyiya.dao;

import com.bbyiya.model.PStylecoordinate;

public interface PStylecoordinateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PStylecoordinate record);

    int insertSelective(PStylecoordinate record);

    PStylecoordinate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PStylecoordinate record);

    int updateByPrimaryKey(PStylecoordinate record);
}