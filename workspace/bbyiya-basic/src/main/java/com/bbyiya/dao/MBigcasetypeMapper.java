package com.bbyiya.dao;

import com.bbyiya.model.MBigcasetype;

public interface MBigcasetypeMapper {
    int deleteByPrimaryKey(Integer typeid);

    int insert(MBigcasetype record);

    int insertSelective(MBigcasetype record);

    MBigcasetype selectByPrimaryKey(Integer typeid);

    int updateByPrimaryKeySelective(MBigcasetype record);

    int updateByPrimaryKey(MBigcasetype record);
}