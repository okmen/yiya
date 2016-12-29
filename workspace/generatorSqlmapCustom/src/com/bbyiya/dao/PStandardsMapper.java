package com.bbyiya.dao;

import com.bbyiya.model.PStandards;

public interface PStandardsMapper {
    int deleteByPrimaryKey(Long standardid);

    int insert(PStandards record);

    int insertSelective(PStandards record);

    PStandards selectByPrimaryKey(Long standardid);

    int updateByPrimaryKeySelective(PStandards record);

    int updateByPrimaryKey(PStandards record);
}