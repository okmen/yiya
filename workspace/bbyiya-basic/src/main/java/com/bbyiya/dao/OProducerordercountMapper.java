package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OProducerordercount;

public interface OProducerordercountMapper {
    int deleteByPrimaryKey(String userorderid);

    int insert(OProducerordercount record);

    int insertSelective(OProducerordercount record);

    OProducerordercount selectByPrimaryKey(String userorderid);

    int updateByPrimaryKeySelective(OProducerordercount record);

    int updateByPrimaryKey(OProducerordercount record);
    
    Integer getMaxOrderIndexByProducerIdAndUserId(@Param("produceruserid")Long produceruserid,@Param("userid")Long userid);
}