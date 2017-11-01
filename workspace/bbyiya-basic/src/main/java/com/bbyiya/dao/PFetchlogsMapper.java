package com.bbyiya.dao;

import com.bbyiya.model.PFetchlogs;

public interface PFetchlogsMapper {
    int deleteByPrimaryKey(String url);

    int insert(PFetchlogs record);

    int insertSelective(PFetchlogs record);

    PFetchlogs selectByPrimaryKey(String url);

    int updateByPrimaryKeySelective(PFetchlogs record);

    int updateByPrimaryKey(PFetchlogs record);
}