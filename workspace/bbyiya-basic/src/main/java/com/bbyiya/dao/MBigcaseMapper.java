package com.bbyiya.dao;

import com.bbyiya.model.MBigcase;

public interface MBigcaseMapper {
    int deleteByPrimaryKey(Integer caseid);

    int insert(MBigcase record);

    int insertSelective(MBigcase record);

    MBigcase selectByPrimaryKey(Integer caseid);

    int updateByPrimaryKeySelective(MBigcase record);

    int updateByPrimaryKey(MBigcase record);
    
    
}