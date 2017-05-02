package com.bbyiya.dao;

import com.bbyiya.model.MBigcasesummary;

public interface MBigcasesummaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MBigcasesummary record);

    int insertSelective(MBigcasesummary record);

    MBigcasesummary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MBigcasesummary record);

    int updateByPrimaryKeyWithBLOBs(MBigcasesummary record);

    int updateByPrimaryKey(MBigcasesummary record);
}