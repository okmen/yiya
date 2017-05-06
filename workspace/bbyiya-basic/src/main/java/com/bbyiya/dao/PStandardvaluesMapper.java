package com.bbyiya.dao;

import com.bbyiya.model.PStandardvalues;

public interface PStandardvaluesMapper {
    int deleteByPrimaryKey(Long standardvalueid);

    int insert(PStandardvalues record);

    int insertSelective(PStandardvalues record);

    PStandardvalues selectByPrimaryKey(Long standardvalueid);

    int updateByPrimaryKeySelective(PStandardvalues record);

    int updateByPrimaryKey(PStandardvalues record);
}