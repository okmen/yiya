package com.bbyiya.dao;

import com.bbyiya.model.MBigcaseexp;

public interface MBigcaseexpMapper {
    int deleteByPrimaryKey(Integer caseid);

    int insert(MBigcaseexp record);

    int insertSelective(MBigcaseexp record);

    MBigcaseexp selectByPrimaryKey(Integer caseid);

    int updateByPrimaryKeySelective(MBigcaseexp record);

    int updateByPrimaryKey(MBigcaseexp record);
}