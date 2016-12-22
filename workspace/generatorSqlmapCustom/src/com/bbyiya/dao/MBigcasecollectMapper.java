package com.bbyiya.dao;

import com.bbyiya.model.MBigcasecollect;

public interface MBigcasecollectMapper {
    int deleteByPrimaryKey(Long collectid);

    int insert(MBigcasecollect record);

    int insertSelective(MBigcasecollect record);

    MBigcasecollect selectByPrimaryKey(Long collectid);

    int updateByPrimaryKeySelective(MBigcasecollect record);

    int updateByPrimaryKey(MBigcasecollect record);
}