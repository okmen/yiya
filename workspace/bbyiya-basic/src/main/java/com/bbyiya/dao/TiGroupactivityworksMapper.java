package com.bbyiya.dao;

import com.bbyiya.model.TiGroupactivityworks;

public interface TiGroupactivityworksMapper {
    int deleteByPrimaryKey(Long workid);

    int insert(TiGroupactivityworks record);

    int insertSelective(TiGroupactivityworks record);

    TiGroupactivityworks selectByPrimaryKey(Long workid);

    int updateByPrimaryKeySelective(TiGroupactivityworks record);

    int updateByPrimaryKey(TiGroupactivityworks record);
}