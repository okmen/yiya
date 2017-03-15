package com.bbyiya.dao;

import com.bbyiya.model.OPayorder;

public interface OPayorderMapper {
    int deleteByPrimaryKey(String payid);

    int insert(OPayorder record);

    int insertSelective(OPayorder record);

    OPayorder selectByPrimaryKey(String payid);

    int updateByPrimaryKeySelective(OPayorder record);

    int updateByPrimaryKey(OPayorder record);
}