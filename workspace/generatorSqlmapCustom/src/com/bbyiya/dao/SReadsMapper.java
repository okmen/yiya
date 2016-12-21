package com.bbyiya.dao;

import com.bbyiya.model.SReads;

public interface SReadsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SReads record);

    int insertSelective(SReads record);

    SReads selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SReads record);

    int updateByPrimaryKeyWithBLOBs(SReads record);

    int updateByPrimaryKey(SReads record);
}