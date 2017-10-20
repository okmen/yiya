package com.bbyiya.dao;

import com.bbyiya.model.TiMyworks;

public interface TiMyworksMapper {
    int deleteByPrimaryKey(Long workid);

    int insert(TiMyworks record);

    int insertSelective(TiMyworks record);

    TiMyworks selectByPrimaryKey(Long workid);

    int updateByPrimaryKeySelective(TiMyworks record);

    int updateByPrimaryKey(TiMyworks record);

}