package com.bbyiya.dao;

import com.bbyiya.model.MBigcasestagesummary;

public interface MBigcasestagesummaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MBigcasestagesummary record);

    int insertSelective(MBigcasestagesummary record);

    MBigcasestagesummary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MBigcasestagesummary record);

    int updateByPrimaryKeyWithBLOBs(MBigcasestagesummary record);

    int updateByPrimaryKey(MBigcasestagesummary record);
}