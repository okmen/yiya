package com.bbyiya.dao;

import com.bbyiya.model.TiGroupactivitypraiseusers;

public interface TiGroupactivitypraiseusersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiGroupactivitypraiseusers record);

    int insertSelective(TiGroupactivitypraiseusers record);

    TiGroupactivitypraiseusers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiGroupactivitypraiseusers record);

    int updateByPrimaryKey(TiGroupactivitypraiseusers record);
}