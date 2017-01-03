package com.bbyiya.dao;

import com.bbyiya.model.OPayforuserorder;

public interface OPayforuserorderMapper {
    int deleteByPrimaryKey(String payid);

    int insert(OPayforuserorder record);

    int insertSelective(OPayforuserorder record);

    OPayforuserorder selectByPrimaryKey(String payid);

    int updateByPrimaryKeySelective(OPayforuserorder record);

    int updateByPrimaryKey(OPayforuserorder record);
}