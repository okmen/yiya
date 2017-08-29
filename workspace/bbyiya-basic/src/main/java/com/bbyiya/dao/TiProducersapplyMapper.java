package com.bbyiya.dao;

import com.bbyiya.model.TiProducersapply;

public interface TiProducersapplyMapper {
    int deleteByPrimaryKey(Long produceruserid);

    int insert(TiProducersapply record);

    int insertSelective(TiProducersapply record);

    TiProducersapply selectByPrimaryKey(Long produceruserid);

    int updateByPrimaryKeySelective(TiProducersapply record);

    int updateByPrimaryKey(TiProducersapply record);
}