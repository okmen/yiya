package com.bbyiya.dao;

import com.bbyiya.model.SMusicrecommend;

public interface SMusicrecommendMapper {
    int deleteByPrimaryKey(Integer reid);

    int insert(SMusicrecommend record);

    int insertSelective(SMusicrecommend record);

    SMusicrecommend selectByPrimaryKey(Integer reid);

    int updateByPrimaryKeySelective(SMusicrecommend record);

    int updateByPrimaryKey(SMusicrecommend record);
}