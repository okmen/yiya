package com.bbyiya.dao;

import com.bbyiya.model.TiActivitys;

public interface TiActivitysMapper {
    int deleteByPrimaryKey(Integer actid);

    int insert(TiActivitys record);

    int insertSelective(TiActivitys record);

    TiActivitys selectByPrimaryKey(Integer actid);

    int updateByPrimaryKeySelective(TiActivitys record);

    int updateByPrimaryKeyWithBLOBs(TiActivitys record);

    int updateByPrimaryKey(TiActivitys record);
}