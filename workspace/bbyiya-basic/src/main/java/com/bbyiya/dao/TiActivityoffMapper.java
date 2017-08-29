package com.bbyiya.dao;

import com.bbyiya.model.TiActivityoff;

public interface TiActivityoffMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiActivityoff record);

    int insertSelective(TiActivityoff record);

    TiActivityoff selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiActivityoff record);

    int updateByPrimaryKey(TiActivityoff record);
}