package com.bbyiya.dao;

import com.bbyiya.model.RCity;

public interface RCityMapper {
    int deleteByPrimaryKey(Integer code);

    int insert(RCity record);

    int insertSelective(RCity record);

    RCity selectByPrimaryKey(Integer code);

    int updateByPrimaryKeySelective(RCity record);

    int updateByPrimaryKey(RCity record);
}