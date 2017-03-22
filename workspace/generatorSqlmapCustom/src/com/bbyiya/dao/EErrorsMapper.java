package com.bbyiya.dao;

import com.bbyiya.model.EErrors;

public interface EErrorsMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(EErrors record);

    int insertSelective(EErrors record);

    EErrors selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(EErrors record);

    int updateByPrimaryKey(EErrors record);
}