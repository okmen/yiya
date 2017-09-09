package com.bbyiya.dao;

import com.bbyiya.model.TiStylecoordinate;

public interface TiStylecoordinateMapper {
    int deleteByPrimaryKey(Long styleid);

    int insert(TiStylecoordinate record);

    int insertSelective(TiStylecoordinate record);

    TiStylecoordinate selectByPrimaryKey(Long styleid);

    int updateByPrimaryKeySelective(TiStylecoordinate record);

    int updateByPrimaryKey(TiStylecoordinate record);
}