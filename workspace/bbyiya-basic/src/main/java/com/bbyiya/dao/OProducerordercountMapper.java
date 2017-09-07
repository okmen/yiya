package com.bbyiya.dao;

import com.bbyiya.model.OProducerordercount;

public interface OProducerordercountMapper {
    int deleteByPrimaryKey(String userorderid);

    int insert(OProducerordercount record);

    int insertSelective(OProducerordercount record);

    OProducerordercount selectByPrimaryKey(String userorderid);

    int updateByPrimaryKeySelective(OProducerordercount record);

    int updateByPrimaryKey(OProducerordercount record);
}