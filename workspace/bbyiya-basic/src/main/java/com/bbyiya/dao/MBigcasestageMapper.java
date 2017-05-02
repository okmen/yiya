package com.bbyiya.dao;

import com.bbyiya.model.MBigcasestage;

public interface MBigcasestageMapper {
    int deleteByPrimaryKey(Integer stageid);

    int insert(MBigcasestage record);

    int insertSelective(MBigcasestage record);

    MBigcasestage selectByPrimaryKey(Integer stageid);

    int updateByPrimaryKeySelective(MBigcasestage record);

    int updateByPrimaryKey(MBigcasestage record);
}