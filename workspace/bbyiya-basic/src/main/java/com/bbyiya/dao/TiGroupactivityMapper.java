package com.bbyiya.dao;

import com.bbyiya.model.TiGroupactivity;

public interface TiGroupactivityMapper {
    int deleteByPrimaryKey(Integer gactid);

    int insert(TiGroupactivity record);

    int insertSelective(TiGroupactivity record);

    TiGroupactivity selectByPrimaryKey(Integer gactid);

    int updateByPrimaryKeySelective(TiGroupactivity record);

    int updateByPrimaryKeyWithBLOBs(TiGroupactivity record);

    int updateByPrimaryKey(TiGroupactivity record);
}