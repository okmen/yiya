package com.bbyiya.dao;

import com.bbyiya.model.MBigcaseclass;

public interface MBigcaseclassMapper {
    int deleteByPrimaryKey(Integer typeid);

    int insert(MBigcaseclass record);

    int insertSelective(MBigcaseclass record);

    MBigcaseclass selectByPrimaryKey(Integer typeid);

    int updateByPrimaryKeySelective(MBigcaseclass record);

    int updateByPrimaryKey(MBigcaseclass record);
}