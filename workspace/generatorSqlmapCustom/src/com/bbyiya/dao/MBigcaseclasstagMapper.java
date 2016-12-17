package com.bbyiya.dao;

import com.bbyiya.model.MBigcaseclasstag;

public interface MBigcaseclasstagMapper {
    int deleteByPrimaryKey(Integer tagid);

    int insert(MBigcaseclasstag record);

    int insertSelective(MBigcaseclasstag record);

    MBigcaseclasstag selectByPrimaryKey(Integer tagid);

    int updateByPrimaryKeySelective(MBigcaseclasstag record);

    int updateByPrimaryKey(MBigcaseclasstag record);
}