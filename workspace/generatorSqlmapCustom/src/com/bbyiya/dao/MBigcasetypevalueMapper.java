package com.bbyiya.dao;

import com.bbyiya.model.MBigcasetypevalue;

public interface MBigcasetypevalueMapper {
    int deleteByPrimaryKey(Integer valueid);

    int insert(MBigcasetypevalue record);

    int insertSelective(MBigcasetypevalue record);

    MBigcasetypevalue selectByPrimaryKey(Integer valueid);

    int updateByPrimaryKeySelective(MBigcasetypevalue record);

    int updateByPrimaryKey(MBigcasetypevalue record);
}