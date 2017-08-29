package com.bbyiya.dao;

import com.bbyiya.model.TiUserdiscounts;

public interface TiUserdiscountsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiUserdiscounts record);

    int insertSelective(TiUserdiscounts record);

    TiUserdiscounts selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiUserdiscounts record);

    int updateByPrimaryKey(TiUserdiscounts record);
}