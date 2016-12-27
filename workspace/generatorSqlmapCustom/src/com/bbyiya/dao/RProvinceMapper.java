package com.bbyiya.dao;

import com.bbyiya.model.RProvince;

public interface RProvinceMapper {
    int deleteByPrimaryKey(Integer code);

    int insert(RProvince record);

    int insertSelective(RProvince record);

    RProvince selectByPrimaryKey(Integer code);

    int updateByPrimaryKeySelective(RProvince record);

    int updateByPrimaryKey(RProvince record);
}