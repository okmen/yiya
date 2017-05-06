package com.bbyiya.dao;

import com.bbyiya.model.ULoginlogs;

public interface ULoginlogsMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(ULoginlogs record);

    int insertSelective(ULoginlogs record);

    ULoginlogs selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(ULoginlogs record);

    int updateByPrimaryKey(ULoginlogs record);
}