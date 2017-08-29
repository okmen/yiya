package com.bbyiya.dao;

import com.bbyiya.model.TiProducers;

public interface TiProducersMapper {
    int deleteByPrimaryKey(Long produceruserid);

    int insert(TiProducers record);

    int insertSelective(TiProducers record);

    TiProducers selectByPrimaryKey(Long produceruserid);

    int updateByPrimaryKeySelective(TiProducers record);

    int updateByPrimaryKey(TiProducers record);
}