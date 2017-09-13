package com.bbyiya.dao;

import com.bbyiya.model.TiStyleadverts;

public interface TiStyleadvertsMapper {
    int deleteByPrimaryKey(Long styleid);

    int insert(TiStyleadverts record);

    int insertSelective(TiStyleadverts record);

    TiStyleadverts selectByPrimaryKey(Long styleid);

    int updateByPrimaryKeySelective(TiStyleadverts record);

    int updateByPrimaryKey(TiStyleadverts record);
}