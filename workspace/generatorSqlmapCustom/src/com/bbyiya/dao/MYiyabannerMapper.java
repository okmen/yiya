package com.bbyiya.dao;

import com.bbyiya.model.MYiyabanner;

public interface MYiyabannerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MYiyabanner record);

    int insertSelective(MYiyabanner record);

    MYiyabanner selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MYiyabanner record);

    int updateByPrimaryKey(MYiyabanner record);
}