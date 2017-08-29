package com.bbyiya.dao;

import com.bbyiya.model.TiActivityworks;

public interface TiActivityworksMapper {
    int deleteByPrimaryKey(Long workid);

    int insert(TiActivityworks record);

    int insertSelective(TiActivityworks record);

    TiActivityworks selectByPrimaryKey(Long workid);

    int updateByPrimaryKeySelective(TiActivityworks record);

    int updateByPrimaryKey(TiActivityworks record);
}