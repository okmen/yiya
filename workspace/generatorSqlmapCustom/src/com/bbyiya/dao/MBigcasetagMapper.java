package com.bbyiya.dao;

import com.bbyiya.model.MBigcasetag;

public interface MBigcasetagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MBigcasetag record);

    int insertSelective(MBigcasetag record);

    MBigcasetag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MBigcasetag record);

    int updateByPrimaryKeyWithBLOBs(MBigcasetag record);

    int updateByPrimaryKey(MBigcasetag record);
}