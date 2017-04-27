package com.bbyiya.dao;

import com.bbyiya.model.MBigcaseuserread;

public interface MBigcaseuserreadMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MBigcaseuserread record);

    int insertSelective(MBigcaseuserread record);

    MBigcaseuserread selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MBigcaseuserread record);

    int updateByPrimaryKey(MBigcaseuserread record);
}